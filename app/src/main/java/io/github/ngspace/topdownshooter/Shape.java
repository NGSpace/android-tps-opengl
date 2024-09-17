package io.github.ngspace.topdownshooter;

public interface Shape {

    public void draw(float[] mvpMatrix);
    public void touchDown(float x, float y);
    public void touchDrag(float x, float y);
    public void touchUp(float x, float y);
}
