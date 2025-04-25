package io.github.ngspace.topdownshooter.gameobjects;

import java.util.function.Consumer;
import java.util.function.BiFunction;

import io.github.ngspace.topdownshooter.renderer.renderer.TextureInfo;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;

public class TriggerGameObject extends Sprite {
    private Consumer<AGameObject> triggerr;
    private BiFunction<AGameObject, TriggerGameObject, Boolean> selfDestroyCondition = (o,t)->false;

    public TriggerGameObject(TextureInfo texture, float x, float y, float width, float height, Consumer<AGameObject> trigger) {
        super(texture, x, y, width, height);
        this.triggerr = trigger;
        this.trigger = true;
    }

    public TriggerGameObject(float x, float y, float width, float height, Consumer<AGameObject> trigger) {
        this(Textures.STARSET, x, y, width, height, trigger);
        texture.setAlpha(0);
    }

    @Override
    public void collidedWith(AGameObject collider) {
        triggerr.accept(collider);
        if (selfDestroyCondition.apply(collider, this))
            scene.destroyObject(this);
    }

    public void setSelfDestroyCondition(BiFunction<AGameObject, TriggerGameObject, Boolean> selfDestroyCondition) {
        this.selfDestroyCondition = selfDestroyCondition;
    }
}
