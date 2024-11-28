package io.github.ngspace.topdownshooter;

import io.github.ngspace.topdownshooter.gameobjects.MovementPhysicsSprite;
import io.github.ngspace.topdownshooter.gameobjects.Sprite;
import io.github.ngspace.topdownshooter.renderer.GameScene;
import io.github.ngspace.topdownshooter.gameobjects.JoyStick;
import io.github.ngspace.topdownshooter.renderer.opengl.renderer.Textures;

public class TestGameScene extends GameScene {

    JoyStick joyStick;
    MovementPhysicsSprite player;

    @Override public void start() {
        addObject(joyStick = new JoyStick());
        addCollidableObject(new Sprite(Textures.FUCKOPENGL,300,300,500,500));
        addCollidableObject(player = new MovementPhysicsSprite(Textures.FUCKOPENGL,960,540,50,50));
    }

    @Override public void update(float delta) {
//        renderer.camera.move((int) (joyStick.xvel*10), (int) (joyStick.yvel*10));
        MainActivity.log(joyStick.xvel*10);
        MainActivity.log("RR" + joyStick.xvel*10);
        player.addVelocity(joyStick.xvel*10, joyStick.yvel*10);
    }
}