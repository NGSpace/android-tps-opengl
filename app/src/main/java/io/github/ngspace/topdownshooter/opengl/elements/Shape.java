package io.github.ngspace.topdownshooter.opengl.elements;

import android.graphics.RectF;
import android.view.MotionEvent;

public interface Shape {

    public void draw(float[] mvpMatrix);

    public RectF getBounds();
    public default boolean intersects(RectF bounds) {return getBounds().intersect(bounds);}
    public default boolean intersects(float x, float y) {return getBounds().contains(x, y);}

    public void touchDown(MotionEvent e, float x, float y);
    public void touchDrag(MotionEvent e, float x, float y);
    public void touchUp(MotionEvent e, float x, float y);
}
