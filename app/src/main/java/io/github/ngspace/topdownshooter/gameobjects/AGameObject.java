package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.utils.Bounds;
import io.github.ngspace.topdownshooter.renderer.elements.InvisibleShape;

public abstract class AGameObject extends InvisibleShape {
    public AGameObject(Bounds bounds) {
        super(bounds);
    }

    public void init(GameScene scene) {
        scene.getRenderer().addTouchElement(this);
    }
}
