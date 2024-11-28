package io.github.ngspace.topdownshooter.renderer.opengl.elements;

import io.github.ngspace.topdownshooter.renderer.opengl.Bounds;

public class InvisibleShape extends Shape {

    private Bounds bounds;

    public InvisibleShape(Bounds bounds) {this.bounds = bounds;}

    @Override public void draw(float[] mvpMatrix) {}

    @Override public Bounds getBounds() {return bounds;}
    @Override public void setBounds(float x, float y, float width, float height) {setBounds(new Bounds(x,y,width,height));}
    @Override public void setBounds(Bounds bounds) {this.bounds = bounds;}
}
