package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.renderer.GameScene;
import io.github.ngspace.topdownshooter.renderer.opengl.renderer.TextureInfo;

public class HudSprite extends Sprite {
    public HudSprite(TextureInfo texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height);
    }
    @Override public void init(GameScene scene) {
        scene.getRenderer().addHudTouchElement(this);
        scene.getRenderer().addHudElement(texture);
    }
}