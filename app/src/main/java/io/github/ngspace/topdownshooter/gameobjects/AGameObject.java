package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.renderer.GameScene;
import io.github.ngspace.topdownshooter.renderer.opengl.Bounds;
import io.github.ngspace.topdownshooter.renderer.opengl.elements.InvisibleShape;

public abstract class AGameObject extends InvisibleShape {
    public AGameObject(Bounds bounds) {
        super(bounds);
    }

    public void init(GameScene scene) {
        scene.getRenderer().addTouchElement(this);
    }
}
