package io.github.ngspace.topdownshooter.gameobjects;

import java.util.function.Consumer;

import io.github.ngspace.topdownshooter.renderer.renderer.TextureInfo;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;

public class TriggerGameObject extends Sprite {
    private Consumer<AGameObject> trigger;

    public TriggerGameObject(TextureInfo texture, float x, float y, float width, float height, Consumer<AGameObject> trigger) {
        super(texture, x, y, width, height);
        this.trigger = trigger;
        collidable = true;
    }

    public TriggerGameObject(float x, float y, float width, float height, Consumer<AGameObject> trigger) {
        this(Textures.STARSET, x, y, width, height, trigger);
        texture.setAlpha(0);
    }

    @Override
    public void collidedWith(AGameObject collider) {
        trigger.accept(collider);
    }
}
