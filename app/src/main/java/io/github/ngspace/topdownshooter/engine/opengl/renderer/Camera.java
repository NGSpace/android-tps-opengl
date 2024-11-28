package io.github.ngspace.topdownshooter.engine.opengl.renderer;

import android.opengl.Matrix;

public class Camera {

    private final float[] mProjectionMatrix = new float[16];
    private float x = 0;
    private float y = 0;

    public Camera() {
        Matrix.frustumM(mProjectionMatrix, 0, -1, 1, -1f, 1f, 3f, 7);
    }

    public void move(int x, int y) {
        this.x+=x;
        this.y+=y;
        setProjection(this.x,this.y);
    }
    public void setProjection(float x, float y) {
        this.x=x;
        this.y=y;
        Matrix.frustumM(mProjectionMatrix, 0, -1-x, 1-x, -1f+y, 1f+y, 3f, 7);
    }
    public float[] getProjectionMatrix() {
        return mProjectionMatrix;
    }

    public void updateProjection() {
    }
}
