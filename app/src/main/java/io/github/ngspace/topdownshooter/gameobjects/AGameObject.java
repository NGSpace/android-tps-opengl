package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.renderer.elements.InvisibleElement;
import io.github.ngspace.topdownshooter.utils.Bounds;

public abstract class AGameObject extends InvisibleElement {
    protected GameScene scene;
    public AGameObject(Bounds bounds) {super(bounds);}

    public void init(GameScene scene) {this.scene=scene;scene.getRenderer().addTouchElement(this);}
    public void destroy(GameScene scene) {scene.getRenderer().removeTouchElement(this);}
    public void collidedWith(AGameObject collider) {
        //Rarely used, keeping optional.
    }
}
