package io.github.ngspace.topdownshooter;

public interface Shape {

    public float w = 2, h = 2;
    public void draw(float[] mvpMatrix);
    public void touch(float x, float y);
}
