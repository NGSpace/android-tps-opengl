package io.github.ngspace.topdownshooter;

public interface Shape {

    public void draw(float[] mvpMatrix);
    public void touch(float x, float y);
}
