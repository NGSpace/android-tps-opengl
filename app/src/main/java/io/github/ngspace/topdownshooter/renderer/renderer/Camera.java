package io.github.ngspace.topdownshooter.renderer.renderer;

import android.graphics.Point;
import android.opengl.GLES32;
import android.opengl.Matrix;

import io.github.ngspace.topdownshooter.gameobjects.MovementPhysicsSprite;
import io.github.ngspace.topdownshooter.renderer.OpenGLActivity;
import io.github.ngspace.topdownshooter.renderer.OpenGLSurfaceView;
import io.github.ngspace.topdownshooter.renderer.elements.Shape;
import io.github.ngspace.topdownshooter.utils.Logcat;

public class Camera {

    private int viewportBuffer;
    private float[] projectionMatrix = new float[16];
    private float[] hudProjectionMatrix = new float[16];
    private float x = 0;
    private float y = 0;
    private Shape center = null;

    public Camera() {setProjection(0,0);}

    public void move(float x, float y) {
        this.x-=x;
        this.y-=y;
        setProjection(this.x,this.y);
    }
    public void setProjection(float x, float y) {
        this.x=x;
        this.y=y;
        Matrix.frustumM(projectionMatrix, 0, -960f-x, 960f-x, -540f+y, 540f+y, 3f, 7);
        Matrix.frustumM(hudProjectionMatrix, 0, -960f, 960f, -540f, 540f, 3f, 7);
    }
    public float[] getProjectionMatrix() {return projectionMatrix;}
    public float[] getHudProjectionMatrix() {return hudProjectionMatrix;}
    public void fixProjection() {setProjection(x,y);}
    public void update() {
        if (center!=null) setProjection(960f-center.getX()-center.getWidth()/2,540f-center.getY()- center.getHeight()/2f);
    }

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

    public float getX() {return x;}
    public float getY() {return y;}

    public void centerOn(Shape center) {this.center = center;}
}