package io.github.ngspace.topdownshooter.renderer.opengl.renderer;

import android.graphics.Point;
import android.opengl.GLES32;
import android.opengl.Matrix;

import io.github.ngspace.topdownshooter.renderer.opengl.OpenGLActivity;
import io.github.ngspace.topdownshooter.renderer.opengl.OpenGLSurfaceView;

public class Camera {

    private int viewportBuffer;
    private float[] projectionMatrix = new float[16];
    private float[] hudProjectionMatrix = new float[16];
    private int x = 0;
    private int y = 0;

    public Camera() {setProjection(0,0);}

    public void move(int x, int y) {
        this.x-=x;
        this.y-=y;
        setProjection(this.x,this.y);
    }
    public void setProjection(int x, int y) {
        this.x=x;
        this.y=y;
        Matrix.frustumM(projectionMatrix, 0, -960f-x, 960f-x, -540f+y, 540f+y, 3f, 7);
        Matrix.frustumM(hudProjectionMatrix, 0, -960f, 960f, -540f, 540f, 3f, 7);
    }
    public float[] getProjectionMatrix() {return projectionMatrix;}
    public float[] getHudProjectionMatrix() {return hudProjectionMatrix;}
    public void updateProjection() {setProjection(x,y);}

    public Point toViewport(int x, int y) {
        return new Point((int) ((((x-viewportBuffer/2f)/(OpenGLActivity.realWidth-viewportBuffer))*2f)*960f-this.x),
                (int) (((float) y /OpenGLActivity.realHeight)*2f*540-this.y));
    }
    public Point toHudViewport(int x, int y) {
        return new Point((int) ((((x-viewportBuffer/2f)/(OpenGLActivity.realWidth-viewportBuffer))*2f)*960f),
                (int) (((float) y /OpenGLActivity.realHeight)*2f*540f));
    }

    public void updateViewport(OpenGLSurfaceView context) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        float height1 = context.getHeight();
        float relation = height1/1080;
        viewportBuffer = (int) (context.getWidth() - (1920*relation));
        GLES32.glViewport(viewportBuffer/2,0, (int) (1920*relation), (int) (1080*relation));
    }

    public int getX() {return x;}
    public int getY() {return y;}
}
