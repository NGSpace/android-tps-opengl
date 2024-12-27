package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.renderer.renderer.TextureInfo;

public class PathFindingEntity extends Entity {
    public PathFindingEntity(TextureInfo texture, float x, float y, float width, float height) {
        super(texture, x, y, width, height);
    }

    public void setTarget(int[][] grid, float playerX, float playerX1) {
    }
}
