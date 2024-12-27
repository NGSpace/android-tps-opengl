package io.github.ngspace.topdownshooter.renderer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.ngspace.topdownshooter.renderer.elements.Element;
import io.github.ngspace.topdownshooter.renderer.renderer.GLRenderer;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class OpenGLSurfaceView extends GLSurfaceView {

    private final GLRenderer renderer;

    List<Element> liftedElements = new ArrayList<Element>();

    public OpenGLSurfaceView(Context context) {
        super(context);
        // Create an OpenGL ES 3.0 context.
        setEGLContextClientVersion(3);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer = new GLRenderer(this));

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @SuppressLint("ClickableViewAccessibility")
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

    private boolean processTouch(MotionEvent e, int x, int y, List<Element> elements) {
        final List<Element> reversedelements = new ArrayList<>(elements);
        Collections.reverse(reversedelements);
        for (Element s : reversedelements) {
            if (!s.isHidden()&&s.contains(x,y)) {
                if (s.touchDown(e, x, y)) {
                    liftedElements.add(s);
                    return true;
                }
            }
        }
        return false;
    }
    public GLRenderer getRenderer() {return renderer;}

    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }
}