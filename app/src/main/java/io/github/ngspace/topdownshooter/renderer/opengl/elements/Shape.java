package io.github.ngspace.topdownshooter.renderer.opengl.elements;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import io.github.ngspace.topdownshooter.renderer.opengl.Bounds;
import io.github.ngspace.topdownshooter.renderer.opengl.TouchEventListener;

public abstract class Shape {

    protected boolean pressed = false;
    protected boolean hidden = false;
    protected List<TouchEventListener> downListeners = new ArrayList<TouchEventListener>();
    protected List<TouchEventListener> dragListeners = new ArrayList<TouchEventListener>();
    protected List<TouchEventListener> upListeners = new ArrayList<TouchEventListener>();

    public void render(float[] mvpMatrix) {if (!isHidden()) draw(mvpMatrix);}
    public abstract void draw(float[] mvpMatrix);

    public boolean procTouchDrag(MotionEvent e, int x, int y) {return touchDrag(e,x,y);}
    public boolean procTouchUp(MotionEvent e, int x, int y) {if (touchUp(e,x,y)) {pressed = false;return true;} return false;}

    public boolean touchDown(MotionEvent e, int x, int y) {
        pressed = true;
        for (TouchEventListener listener : downListeners) listener.exec(this,x,y);
        return true;
    }
    public boolean touchDrag(MotionEvent e, int x, int y) {
        for (TouchEventListener listener : dragListeners) listener.exec(this,x,y);
        return true;
    }
    public boolean touchUp(MotionEvent e, int x, int y) {
        pressed = false;
        for (TouchEventListener listener : upListeners) listener.exec(this,x,y);
        return true;
    }

    // Listeners
    public void addTouchDownListener(TouchEventListener listener) {downListeners.add(listener);}
    public void addTouchDragListener(TouchEventListener listener) {dragListeners.add(listener);}
    public void addTouchUpListener(TouchEventListener listener) {upListeners.add(listener);}

    public boolean isPressed() {return pressed;}
    public boolean isHidden() {return hidden;}

    public void setHidden(boolean hidden) {this.hidden = hidden;}

    public abstract Bounds getBounds();
    public abstract void setBounds(float x, float y, float width, float height);
    public void setBounds(Bounds bounds) {setBounds(bounds.x(), bounds.y(), bounds.width(), bounds.height());}

    public boolean intersects(Bounds bounds) {return getBounds().intersects(bounds);}
    public boolean contains(int x, int y) {return getBounds().contains(x, y);}

    public float getX() {return  getBounds().x();}
    public float getY() {return  getBounds().y();}
    public float getWidth() {return  getBounds().width();}
    public float getHeight() {return  getBounds().height();}
}
