package io.github.ngspace.topdownshooter;

import android.content.Intent;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.gameobjects.Bullet;
import io.github.ngspace.topdownshooter.gameobjects.Entity;
import io.github.ngspace.topdownshooter.gameobjects.HudSprite;
import io.github.ngspace.topdownshooter.gameobjects.HudTextElement;
import io.github.ngspace.topdownshooter.gameobjects.JoyStick;
import io.github.ngspace.topdownshooter.gameobjects.Sprite;
import io.github.ngspace.topdownshooter.gameobjects.Torrent;
import io.github.ngspace.topdownshooter.levelgenerator.FailedToGenLevelException;
import io.github.ngspace.topdownshooter.levelgenerator.LevelGenerator;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;
import io.github.ngspace.topdownshooter.utils.Logcat;
import io.github.ngspace.topdownshooter.utils.UserData;

public class GeneratedGameScene extends GameScene {

    public int fuelLeftToLeave;
    public AGameObject exitDoor;
    UserData user;

    JoyStick movementStick;
    JoyStick secondaryStick;
    public Entity player;
    Sprite shooting;

    HudSprite hudcover;
    HudTextElement ammoText;
    private HudTextElement healthText;
    private HudTextElement fuelText;

    public static final int SPACE_FROM_CORNER = 150;
    public static final int SPEED = 400;
    public static final int GRID_RES = 20;
    public static final int GRID_PIXEL = 100;
    public static final int playerSize = 100;
    float rotationAngle = -1;
    ArrayList<Bullet> bullets = new ArrayList<>();
    public ArrayList<Torrent> torrents = new ArrayList<>();
    public List<Integer> keys = new ArrayList<>();
    LevelGenerator lvlGen;

    private int totalFuel;
    boolean notGenerated = true;

    @Override public void start() {
        user = (UserData) getIntent().getSerializableExtra("UserData");

        /// Movement joystick
        addObject(movementStick = new JoyStick(SPACE_FROM_CORNER,1080-200-SPACE_FROM_CORNER));
        /// Shooting joystick
        addObject(secondaryStick = new JoyStick(1920-SPACE_FROM_CORNER-50-200,1080-200-SPACE_FROM_CORNER));
        /// Shooting Aiming overlay
        addObject(shooting = new Sprite(Textures.SHOOTING_OVERLAY,910,490,330,2500));

        /// Actual player (As in the entity, is invisible)
        addPhysicsObject(player = new Entity(Textures.WALL,200,-100,playerSize,playerSize));
        player.health = user.health;
        player.addDeathListener(()->nextLevel(false));
        player.setVisible(false);
        /// "Fake" player (As in the one that is shown on screen)
        addObject(new HudSprite(Textures.PLAYER, (1920-playerSize)/2f, (1080-playerSize)/2f, playerSize, playerSize));

        /// Text
        addObject(healthText = new HudTextElement("Health: " + user.health, 0, 0, 50, 60));
        addObject(ammoText = new HudTextElement("Ammo: " + user.ammo, 0, 60, 50, 60));
        addObject(fuelText = new HudTextElement("Fuel: " + fuelLeftToLeave + "/" + totalFuel, 0, 120, 50, 60));

        player.setVisible(false);
        renderer.camera.centerOn(player);



        hudcover = new HudSprite(Textures.GENERATING, 0, 0, 1920, 1080);
        addObject(hudcover);
        try {
            lvlGen = new LevelGenerator(this);
        } catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void update(double delta) {
        if (notGenerated) {
            try {
                lvlGen.generateLevel(this, "Start", 0, 0);
                totalFuel = fuelLeftToLeave;
                notGenerated = false;
                hudcover.setVisible(false);
            } catch (FailedToGenLevelException ignored) {
                ignored.printStackTrace();
            }
        }
        if (movementStick.xvel!=0||movementStick.yvel!=0)
            player.setVelocity(movementStick.xvel*SPEED, movementStick.yvel*SPEED);
        float shootyStickAngle = secondaryStick.getStickAngle();

        //Aiming
        if (shootyStickAngle!=-1) {
            shooting.setVisible(true);
            shooting.setAngle(shootyStickAngle);
            shooting.setLocation(player.getX()-shooting.getWidth()/2+player.getWidth()/2,player.getY()-shooting.getHeight()/2+player.getHeight()/2);
        } else shooting.setVisible(false);

        //Shooting
        if (shootyStickAngle==-1&&rotationAngle!=-1) {
            if (user.ammo>0) {
                user.ammo--;
                ammoText.setText("Ammo: " + user.ammo);
                var bullet = new Bullet(player, rotationAngle, 1);
                bullet.hitListener = p -> {
                    if (p instanceof Torrent torrent)
                        torrents.remove(torrent);
                };
                addBullet(bullet);
            }
        }

        for (Bullet bullet : bullets) {
            bullet.lifetime += delta;
            if (bullet.lifetime >= 10) destroyObject(bullet);
        }
        rotationAngle = shootyStickAngle;



        for (Torrent torrent : torrents) {
            torrent.aimAndShoot(player, this);
        }

        healthText.setText("Health: " + player.health);
        fuelText.setText("Fuel: " + fuelLeftToLeave + "/" + totalFuel);
    }
    float clamp(float value, float min, float max) {return Math.max(min, Math.min(max, value));}

    public void addBullet(Bullet bullet) {
        addPhysicsObject(bullet);
        bullets.add(bullet);
    }

    public void nextLevel(boolean b) {
        Intent intent = new Intent(this, NextLevel.class);
        user.health = player.health;
        intent.putExtra("UserData", user);
        intent.putExtra("Success", b);
        startActivity(intent);
    }

    @Override public void onBackPressed() {}
}