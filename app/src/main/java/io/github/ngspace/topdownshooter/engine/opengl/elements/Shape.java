package io.github.ngspace.topdownshooter.engine.opengl.elements;

import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import io.github.ngspace.topdownshooter.engine.opengl.TouchEventListener;

public abstract class Shape {

    protected boolean pressed = false;
    protected List<TouchEventListener> downListeners = new ArrayList<TouchEventListener>();
    protected List<TouchEventListener> dragListeners = new ArrayList<TouchEventListener>();
    protected List<TouchEventListener> upListeners = new ArrayList<TouchEventListener>();

    public abstract void draw(float[] mvpMatrix);

    public boolean procTouchDrag(MotionEvent e, float x, float y) {return touchDrag(e,x,y);}
    public boolean procTouchUp(MotionEvent e, float x, float y) {if (touchUp(e,x,y)) {pressed = false;return true;} return false;}

    public boolean touchDown(MotionEvent e, float x, float y) {
        pressed = true;
        for (TouchEventListener listener : downListeners) listener.exec(e,x,y);
        return true;
    }
    public boolean touchDrag(MotionEvent e, float x, float y) {
        for (TouchEventListener listener : dragListeners) listener.exec(e,x,y);
        return true;
    }
    public boolean touchUp(MotionEvent e, float x, float y) {
        pressed = false;
        for (TouchEventListener listener : upListeners) listener.exec(e,x,y);
        return true;
    }

    // Listeners
    public void addTouchDownListener(TouchEventListener listener) {downListeners.add(listener);}
    public void addTouchDragListener(TouchEventListener listener) {dragListeners.add(listener);}
    public void addTouchUpListener(TouchEventListener listener) {downListeners.add(listener);}

    public boolean isPressed() {return pressed;}



    public abstract RectF getBounds();
    public abstract void setBounds(float x, float y, float width, float height);

    public boolean intersects(RectF bounds) {return getBounds().intersect(bounds);}
    public boolean intersects(float x, float y) {return getBounds().contains(x, y);}

    public float getX() {return  getBounds().left;}
    public float getY() {return  getBounds().top;}
    public float getWidth() {return  getBounds().right-getBounds().left;}
    public float getHeight() {return  getBounds().bottom-getBounds().top;}
}
