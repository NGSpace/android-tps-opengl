package io.github.ngspace.topdownshooter;

import io.github.ngspace.topdownshooter.gameobjects.MovementPhysicsSprite;
import io.github.ngspace.topdownshooter.gameobjects.Sprite;
import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.gameobjects.JoyStick;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;
import io.github.ngspace.topdownshooter.utils.Logcat;

public class TestGameScene extends GameScene {

    JoyStick movementStick;
    JoyStick secondaryStick;
    MovementPhysicsSprite player;
    Sprite Cube;
    public static final int SPACE_FROM_CORNER = 150;
    public static final int SPEED = 2;

    @Override public void start() {
        addObject(movementStick = new JoyStick(SPACE_FROM_CORNER,1080-200-SPACE_FROM_CORNER));
        addObject(secondaryStick = new JoyStick(1920-SPACE_FROM_CORNER-50-200,1080-200-SPACE_FROM_CORNER));
        addCollidableObject(Cube = new Sprite(Textures.STARSET,300,300,500,500));
        addCollidableObject(player = new MovementPhysicsSprite(Textures.FUCKOPENGL,860,440,200,200));
        renderer.camera.centerOn(player);
    }

    @Override public void update(float delta) {
        player.setVelocity(movementStick.xvel*SPEED, movementStick.yvel*SPEED);

//        var v = (theta(1920-SPACE_FROM_CORNER-50-200,1080-200-SPACE_FROM_CORNER,secondaryStick.getX(), secondaryStick.getY())-360)*-1;
        var v = secondaryStick.getStickAngle();
        if (v!=-1)
            Cube.setAngle(v);
    }
//    public double r() {
//        double startAngle = Math.atan2(Y1-Cy, X1-Cx);
//        double endAngle = Math.atan2(Y2-Cy, X2-Cx);
//        return endAngle - startAngle;
//    }

}