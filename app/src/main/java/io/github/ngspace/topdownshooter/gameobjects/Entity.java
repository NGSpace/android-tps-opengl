package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.renderer.renderer.TextureInfo;

public class Entity extends MovementPhysicsSprite {
    public Entity(TextureInfo texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height);
    }
}
