package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.engine.GameScene;

public class HudTextElement extends TextElement {
    public HudTextElement(String text, float x, float y, int fontsize, float height) {super(text, x, y, fontsize, height);}
    @Override public void init(GameScene scene) {
        scene.getRenderer().addHudTouchElement(this);
        scene.getRenderer().addHudElement(text);
    }
    @Override public void destroy(GameScene scene) {
        scene.getRenderer().removeHudTouchElement(this);
        scene.getRenderer().removeHudElement(text);
    }
}