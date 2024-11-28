package io.github.ngspace.topdownshooter.renderer.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.ngspace.topdownshooter.MainActivity;
import io.github.ngspace.topdownshooter.renderer.opengl.renderer.GLRenderer;
import io.github.ngspace.topdownshooter.renderer.opengl.elements.Shape;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class OpenGLSurfaceView extends GLSurfaceView {

    private final GLRenderer renderer;

    List<Shape> liftedElements = new ArrayList<Shape>();

    public OpenGLSurfaceView(Context context) {
        super(context);
        // Create an OpenGL ES 3.0 context.
        setEGLContextClientVersion(3);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer = new GLRenderer(this));

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }

    @Override public boolean onTouchEvent(MotionEvent e) {
        var viewport = renderer.toViewport((int)e.getX(),(int)e.getY());
        int x = viewport.x;
        int y = viewport.y;
        var hudviewport = renderer.toHudViewport((int)e.getX(),(int)e.getY());
        int hudx = hudviewport.x;
        int hudy = hudviewport.y;
        int action = e.getActionMasked();
        if (action==MotionEvent.ACTION_DOWN) {
            if (processTouch(e,hudx,hudy,renderer.toptouchelements)) return true;
            if (processTouch(e,x,y,renderer.touchelements)) return true;
        }
        for (var s : liftedElements) {
            boolean hudelement = renderer.toptouchelements.contains(s);
            if (action==MotionEvent.ACTION_MOVE) s.touchDrag(e, hudelement?hudx:x, hudelement?hudy:y);
            if (action==MotionEvent.ACTION_UP) {
                s.touchUp(e, hudelement?hudx:x, hudelement?hudy:y);
                liftedElements.remove(s);
            }
        }
        return !liftedElements.isEmpty();
    }

    private boolean processTouch(MotionEvent e, int x, int y, List<Shape> elements) {
        final List<Shape> reversedelements = new ArrayList<>(elements);
        Collections.reverse(reversedelements);
        for (Shape s : reversedelements) {
            if (s.contains(x,y)) {
                if (s.touchDown(e, x, y)) {
                    liftedElements.add(s);
                    return true;
                }
            }
        }
        return false;
    }
    public GLRenderer getRenderer() {return renderer;}
}