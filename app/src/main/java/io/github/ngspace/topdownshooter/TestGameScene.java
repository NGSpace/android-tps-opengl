package io.github.ngspace.topdownshooter;

import java.util.ArrayList;
import java.util.Arrays;

import io.github.ngspace.topdownshooter.gameobjects.Bullet;
import io.github.ngspace.topdownshooter.gameobjects.Entity;
import io.github.ngspace.topdownshooter.gameobjects.PathFindingEntity;
import io.github.ngspace.topdownshooter.gameobjects.Sprite;
import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.gameobjects.JoyStick;
import io.github.ngspace.topdownshooter.renderer.elements.Element;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;
import io.github.ngspace.topdownshooter.utils.Logcat;

public class TestGameScene extends GameScene {

    JoyStick movementStick;
    JoyStick secondaryStick;
    Entity player;
    Sprite shooting;
    public static final int SPACE_FROM_CORNER = 150;
    public static final int SPEED = 400;
    public static final int GRID_RES = 20;
    public static final int GRID_PIXEL = 100;
    float rotationAngle = -1;
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<PathFindingEntity> enemies = new ArrayList<>();
    int[][] grid;

    @Override public void start() {
        addObject(movementStick = new JoyStick(SPACE_FROM_CORNER,1080-200-SPACE_FROM_CORNER));
        addObject(secondaryStick = new JoyStick(1920-SPACE_FROM_CORNER-50-200,1080-200-SPACE_FROM_CORNER));
        addPhysicsObject(new Sprite(Textures.STARSET,300,200,100,100));
        addPhysicsObject(new Sprite(Textures.STARSET,200,300,100,100));
        addPhysicsObject(new Sprite(Textures.STARSET,100,200,100,100));
        bakeGrid();
        addEnemy(300,300);
        addObject(shooting = new Sprite(Textures.SHOOTING_OVERLAY,910,490,330,2500));
        addPhysicsObject(player = new Entity(Textures.FUCKOPENGL,200,200,100,100));

        for (PathFindingEntity enemy : enemies) {
            float playerX = player.getCenterX();
            float playerY = player.getCenterY();
            enemy.setTarget(clone(grid), playerX, playerY);
        }

        renderer.camera.centerOn(player);
    }

    private int[][] clone(int[][] grid) {
        int[][] newarr = new int[grid.length][];
        for (int i = 0;i<grid.length;i++) {
            int[] subarr = grid[i];
            int[] newsubarr = new int[subarr.length];
            for (int j = 0;j<subarr.length;j++) {
                newsubarr[j]=subarr[j];
            }
            newarr[i]=newsubarr;
        }
        return newarr;
    }

    private void bakeGrid() {
        // Description of the Grid:
        // 1--> The cell is not blocked
        // 0--> The cell is blocked

        final int OFFSETX = 1;
        final int OFFSETY = 1;
        grid = new int[GRID_RES][GRID_RES];
        for (int yloop = 0;yloop<GRID_RES;yloop++) {
            int y = yloop * GRID_PIXEL + OFFSETY;
            for (int xloop = 0;xloop<GRID_RES;xloop++) {
                Element element = renderer.ElementAt(xloop * GRID_PIXEL + OFFSETX,y);
                if (element==null) grid[yloop][xloop] = 0;
                else grid[yloop][xloop] = 1;
            }
        }
    }

    @Override public void update(double delta) {
//        Logcat.log(delta);
        if (movementStick.xvel!=0||movementStick.yvel!=0) player.setVelocity(movementStick.xvel*SPEED, movementStick.yvel*SPEED);
        float shootyStickAngle = secondaryStick.getStickAngle();

        //Aiming
        if (shootyStickAngle!=-1) {
            shooting.setHidden(false);
            shooting.setAngle(shootyStickAngle);
            shooting.setLocation(player.getX()-shooting.getWidth()/2+player.getWidth()/2,player.getY()-shooting.getHeight()/2+player.getHeight()/2);
        } else shooting.setHidden(true);

        //Shooting
        if (shootyStickAngle==-1&&rotationAngle!=-1) {
            addBullet(new Bullet(player, rotationAngle, 1));
        }

        for (Bullet bullet : bullets) {
            bullet.lifetime += delta;
            if (bullet.lifetime >= 10) destroyObject(bullet);
        }

        for (PathFindingEntity enemy : enemies) {
            if (enemy.tick++%100==0) {
                var nxt = enemy.getNext();
                if (nxt==null) {
//                    float playerX = player.getX();
//                    float playerY = player.getY();
//                    enemy.setTarget(grid, playerX, playerY);
//                    nxt = enemy.getNext();
                    enemy.setLocation(enemy.targetpos.x,enemy.targetpos.y);
                    continue;
                }
                if (enemy.targetpos!=null) {
                    enemy.prevpos = enemy.targetpos;
                }

                enemy.targetpos = nxt;

                Logcat.log(nxt.x,nxt.y);
                float playerX = player.getCenterX();
                float playerY = player.getCenterY();
//                enemy.setTarget(clone(grid), playerX, playerY);
                enemy.tick=1;
            }
            if (enemy.targetpos==null) continue;//No more targets? That can't be right!
            //TODO delete enemy when really far away from player
            float enemeyspeed = 60;
//            float velx = clamp(enemy.targetpos.x-enemy.prevpos.x,-enemeyspeed,enemeyspeed);
//            float vely = clamp(,-enemeyspeed,enemeyspeed);
            enemy.setLocation(enemy.getX()+(enemy.targetpos.x-enemy.prevpos.x)/100f, enemy.getY()+(enemy.targetpos.y-enemy.prevpos.y)/100f);

//            enemy.setVelocity(velx, vely);
        }
        rotationAngle = shootyStickAngle;
    }
    float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    private void addBullet(Bullet bullet) {
        addPhysicsObject(bullet);
        bullets.add(bullet);
    }

    private void addEnemy(int x, int y) {
        PathFindingEntity enemy = new PathFindingEntity(Textures.FUCKOPENGL,x,y,100,100);
        addPhysicsObject(enemy);
        enemies.add(enemy);
    }
}