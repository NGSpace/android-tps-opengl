package io.github.ngspace.topdownshooter.gameobjects;

import java.util.function.Consumer;
import java.util.function.BiFunction;

import io.github.ngspace.topdownshooter.renderer.renderer.TextureInfo;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;
import io.github.ngspace.topdownshooter.utils.Bounds;

public class TriggerGameObject extends Sprite {
    private Consumer<AGameObject> triggerr;
    private BiFunction<AGameObject, TriggerGameObject, Boolean> selfDestroyTrigger = (o, t)->false;

    public TriggerGameObject(TextureInfo texture, float x, float y, float width, float height, Consumer<AGameObject> trigger) {
        super(texture, x, y, width, height);
        this.triggerr = trigger;
        this.trigger = true;
    }

    public TriggerGameObject(TextureInfo texture, Bounds bounds, Consumer<AGameObject> trigger) {
        super(texture, bounds);
        this.triggerr = trigger;
        this.trigger = true;
    }

    public TriggerGameObject(float x, float y, float width, float height, Consumer<AGameObject> trigger) {
        this(Textures.WALL, x, y, width, height, trigger);
        setVisible(false);
    }

    @Override
    public void collidedWith(AGameObject collider) {
        triggerr.accept(collider);
        if (selfDestroyTrigger.apply(collider, this))
            scene.destroyObject(this);
    }

    public void setSelfDestroyTrigger(BiFunction<AGameObject, TriggerGameObject, Boolean> selfDestroyCondition) {
        this.selfDestroyTrigger = selfDestroyCondition;
    }
}
