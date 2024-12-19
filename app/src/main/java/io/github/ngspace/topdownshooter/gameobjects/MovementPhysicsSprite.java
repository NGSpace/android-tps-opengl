package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.engine.MovementPhysics;
import io.github.ngspace.topdownshooter.renderer.renderer.TextureInfo;

public class MovementPhysicsSprite extends Sprite implements MovementPhysics {
    private float velx;
    private float vely;

    public MovementPhysicsSprite(TextureInfo texture, float x, float y, float width, float height) {super(texture, x, y, width, height);}

    @Override public float getVelocityX() {return velx;}
    @Override public float getVelocityY() {return vely;}

    @Override public void setVelocity(float x, float y) {this.velx = x; this.vely = y;}

    @Override public int getDrag() {return 1;}

    @Override public void addVelocity(float x, float y) {
        this.velx = clamp(velx+x,-1,1);
        this.vely = clamp(vely+y,-1,1);
    }
    float clamp(float value, float min, float max) {return Math.max(min, Math.min(max, value));}
}
