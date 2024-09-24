package io.github.ngspace.topdownshooter.opengl.elements;

import android.graphics.RectF;

public interface Shape {

    public void draw(float[] mvpMatrix);

    public RectF getBounds();
    public default boolean intersects(RectF bounds) {
        return getBounds().intersect(bounds);
    }
    public default boolean intersects(float x, float y) {
        return getBounds().contains(x, y);
    }

    public void touchDown(float x, float y);
    public void touchDrag(float x, float y);
    public void touchUp(float x, float y);
}
