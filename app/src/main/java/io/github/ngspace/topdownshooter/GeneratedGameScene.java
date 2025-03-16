package io.github.ngspace.topdownshooter;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.gameobjects.Bullet;
import io.github.ngspace.topdownshooter.gameobjects.Entity;
import io.github.ngspace.topdownshooter.gameobjects.JoyStick;
import io.github.ngspace.topdownshooter.gameobjects.PathFindingEntity;
import io.github.ngspace.topdownshooter.gameobjects.Sprite;
import io.github.ngspace.topdownshooter.gameobjects.TriggerGameObject;
import io.github.ngspace.topdownshooter.levelgenerator.LevelReader;
import io.github.ngspace.topdownshooter.renderer.elements.Element;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;
import io.github.ngspace.topdownshooter.utils.Logcat;

public class GeneratedGameScene extends GameScene {

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
        addObject(shooting = new Sprite(Textures.SHOOTING_OVERLAY,910,490,330,2500));
        addPhysicsObject(player = new Entity(Textures.FUCKOPENGL,-50,-50,100,100));
        generateBasicRoom(0,0);
        renderer.camera.centerOn(player);
    }
    int ij = 0;

    private void generateBasicRoom(int x, int y) {
        int d = 500;
        // Bottom wall
        addPhysicsObject(new Sprite(Textures.STARSET, x-d, y+d, d-100, 10));
        addPhysicsObject(new Sprite(Textures.STARSET, x+100, y+d, d-100, 10));
        // Leftside wall
        addPhysicsObject(new Sprite(Textures.STARSET, x-d, y-d, 10, d*2));
        // Rightside wall
        addPhysicsObject(new Sprite(Textures.STARSET, x+d, y-d, 10, d*2));
        // Top wall
        addPhysicsObject(new Sprite(Textures.STARSET, x-d, y-d, d-100, 10));
        addPhysicsObject(new Sprite(Textures.STARSET, x+100, y-d, d-100, 10));

        // Door
        AtomicBoolean b = new AtomicBoolean();
        addPhysicsObject(new TriggerGameObject(Textures.FUCKOPENGL, x-100, y-d, 200, 10, c->{
            if (b.get()) return;
            b.set(true);
            generateBasicRoom(x, y-d*2);
        }));
        try {
            new LevelReader(this);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void update(double delta) {
        if (movementStick.xvel!=0||movementStick.yvel!=0)
            player.setVelocity(movementStick.xvel*SPEED, movementStick.yvel*SPEED);
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
        rotationAngle = shootyStickAngle;
    }
    float clamp(float value, float min, float max) {return Math.max(min, Math.min(max, value));}

    private void addBullet(Bullet bullet) {
        addPhysicsObject(bullet);
        bullets.add(bullet);
    }
}