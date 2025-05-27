package io.github.ngspace.topdownshooter.gameobjects;

import androidx.annotation.NonNull;

import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.renderer.elements.InvisibleElement;
import io.github.ngspace.topdownshooter.utils.Bounds;

public abstract class AGameObject extends InvisibleElement {
    protected GameScene scene;
    protected boolean trigger = false;
    public AGameObject(Bounds bounds) {super(bounds);}

    public void init(GameScene scene) {this.scene=scene;scene.getRenderer().addTouchElement(this);}
    public void destroy(GameScene scene) {}
    public void collidedWith(AGameObject collider) {/* Rarely used, keeping optional. */}

    public boolean isTrigger() {return trigger;}
    public void setTrigger(boolean trigger) {this.trigger = trigger;}

    @NonNull
    @Override
    public String toString() {
        return "AGameObject{" +
                "visible=" + visible +
                "x=" + getX() +
                "y=" + getY() +
                "width=" + getWidth() +
                "height=" + getHeight() +
                '}';
    }
}
