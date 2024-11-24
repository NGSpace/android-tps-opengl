package io.github.ngspace.topdownshooter.engine.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.ngspace.topdownshooter.engine.opengl.elements.Shape;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class OpenGLSurfaceView extends GLSurfaceView {

    private final GLRenderer mRenderer;
    public static Context context;

    public OpenGLSurfaceView(Context context) {
        super(context);
        this.context = context;
        // Create an OpenGL ES 3.0 context.
        setEGLContextClientVersion(3);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer((mRenderer = new GLRenderer(this)));

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    List<Shape> liftedElements = new ArrayList<Shape>();

    @Override public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        var viewport = mRenderer.toViewport(x,y);
        x = viewport.x;
        y = viewport.y;
//        if (e.getAction()!=MotionEvent.ACTION_MOVE) {
//            MainActivity.log(x + "  " + e.getActionMasked());
//            MainActivity.log(e.getAction()& e.ACTION_MASK);
//        }
        if (e.getAction()==MotionEvent.ACTION_DOWN) {
            for (Shape s : reverse(mRenderer.elements)) {
                if (s.intersects(x,y)) {
                    if (s.touchDown(e, x, y)) {
                        liftedElements.add(s);
                        return true;
                    }
                }
            }
        }
        for (var s : liftedElements) {
            if (e.getAction()==MotionEvent.ACTION_MOVE) s.touchDrag(e, x, y);
            if (e.getAction()==MotionEvent.ACTION_UP) {
                s.touchUp(e, x, y);
                liftedElements.remove(s);
            }
        }
        return !liftedElements.isEmpty();
    }
    static <T> List<T> reverse(final List<T> list) {
        final List<T> result = new ArrayList<>(list);
        Collections.reverse(result);
        return result;
    }
    public GLRenderer getRenderer() {return mRenderer;}
}
