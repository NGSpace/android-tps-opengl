package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.renderer.renderer.TextureInfo;

public class Entity extends MovementPhysicsSprite {

    public int health = 100;
    Runnable deathListener = ()->{/* nothing */};

    public Entity(TextureInfo texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height);
    }

    public void damage(int damage) {
        health -= damage;
        if (health<=0) {
            scene.destroyObject(this);
            deathListener.run();
        }
    }

    public void addDeathListener(Runnable listener) {deathListener = listener;}
}