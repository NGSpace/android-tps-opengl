package io.github.ngspace.topdownshooter.renderer.elements;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import io.github.ngspace.topdownshooter.utils.Bounds;
import io.github.ngspace.topdownshooter.renderer.TouchEventListener;

public abstract class Element {

    protected boolean pressed = false;
    protected boolean hidden = false;
    protected List<TouchEventListener> downListeners = new ArrayList<TouchEventListener>();
    protected List<TouchEventListener> dragListeners = new ArrayList<TouchEventListener>();
    protected List<TouchEventListener> upListeners = new ArrayList<TouchEventListener>();
    protected float angle = 0;

    public void render(float[] mvpMatrix) {if (!isHidden()) draw(mvpMatrix);}
    public abstract void draw(float[] mvpMatrix);

    public boolean touchDown(MotionEvent e, int x, int y) {
        pressed = true;
        for (TouchEventListener listener : downListeners) listener.exec(this,x,y);
        return true;
    }
    public void touchDrag(MotionEvent e, int x, int y) {
        for (TouchEventListener listener : dragListeners) listener.exec(this,x,y);
    }
    public void touchUp(MotionEvent e, int x, int y) {
        pressed = false;
        for (TouchEventListener listener : upListeners) listener.exec(this,x,y);
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
    public void setLocation(float x, float y) {setBounds(x, y, getWidth(), getHeight());}
    public void setSize(float width, float height) {setBounds(getX(), getY(), width, height);}

    public boolean intersects(Bounds bounds) {return getBounds().intersects(bounds);}
    public boolean contains(float x, float y) {return getBounds().contains(x, y);}

    public float getX() {return  getBounds().x();}
    public float getY() {return  getBounds().y();}
    public float getWidth() {return  getBounds().width();}
    public float getHeight() {return  getBounds().height();}

    public float getAngle() {return angle;}
    public void setAngle(float angle) {this.angle = angle;}

    public float getCenterX() {return getX()+getWidth ()/2;}
    public float getCenterY() {return getY()+getHeight()/2;}
}
