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

    @Override public void start() {
        addObject(movementStick = new JoyStick(SPACE_FROM_CORNER,1080-200-SPACE_FROM_CORNER));
        addObject(secondaryStick = new JoyStick(1920-SPACE_FROM_CORNER-50-200,1080-200-SPACE_FROM_CORNER));
        addPhysicsObject(new Sprite(Textures.STARSET,300,200,100,100));
        addPhysicsObject(new Sprite(Textures.STARSET,200,300,100,100));
        addPhysicsObject(new Sprite(Textures.STARSET,100,200,100,100));
        bakeGrid();
        addEnemy(510,490);
        addObject(shooting = new Sprite(Textures.SHOOTING_OVERLAY,910,490,330,2500));
        addPhysicsObject(player = new Entity(Textures.FUCKOPENGL,200,200,100,100));
        renderer.camera.centerOn(player);
    }

    private void bakeGrid() {
        final int OFFSET = 1;
        int[][] grid = new int[GRID_RES][GRID_RES];
        for (int xloop = 0;xloop<GRID_RES;xloop++) {
            int x = xloop * GRID_PIXEL - OFFSET;
            for (int yloop = 0;yloop<GRID_RES;yloop++) {
                int y = yloop * GRID_PIXEL - OFFSET;
                Element element = renderer.ElementAt(x,y);
                if (element==null) grid[xloop][yloop] = 0;
                else grid[xloop][yloop] = 1;
            }
        }
        StringBuilder str = new StringBuilder("[");
        for (int[] ints : grid) {
            str.append(Arrays.toString(ints));
            str.append(",\n");
        }
        str.append(']');
        Logcat.log(str.toString());
    }

    @Override public void update(double delta) {
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
            if (++enemy.lifetime%30==0) {
                Logcat.log("Pathfinding for enemy.");

                float playerX = player.getCenterX();
                float playerY = player.getCenterY();
    //                enemy.setTarget(grid, playerX, playerY);
            }
            //TODO delete enemy when really far away from player
//            float enemeyspeed = 40;
//            float velx = clamp(player.getCenterX()-enemy.getCenterX(),-enemeyspeed,enemeyspeed);
//            float vely = clamp(player.getCenterY()-enemy.getCenterY(),-enemeyspeed,enemeyspeed);



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
        PathFindingEntity enemy = new PathFindingEntity(Textures.FUCKOPENGL,x,y,200,200);
        addPhysicsObject(enemy);
        enemies.add(enemy);
    }
}