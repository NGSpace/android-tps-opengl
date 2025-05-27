package io.github.ngspace.topdownshooter.renderer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.ngspace.topdownshooter.renderer.elements.Element;
import io.github.ngspace.topdownshooter.renderer.renderer.GLRenderer;
import android.util.SparseArray; // For tracking lifted elements per pointer

public class OpenGLSurfaceView extends GLSurfaceView {

    private final GLRenderer renderer;

    // Use a SparseArray to map pointer IDs to the Element they are interacting with.
    // This allows each finger to interact with its own element.
    private SparseArray<Element> pointerIdToLiftedElementMap = new SparseArray<>();

    public OpenGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        setRenderer(renderer = new GLRenderer(this));
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getActionMasked();
        int pointerIndex = e.getActionIndex(); // Index of the pointer that caused this event
        int pointerId = e.getPointerId(pointerIndex); // Stable ID of the pointer

        // Coordinates for the pointer that triggered the current event
        // (for DOWN/UP events) or for each pointer in a loop (for MOVE).
        // These will be updated inside loops for ACTION_MOVE.

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // This pointer (identified by pointerId) is now touching the screen.
                // Get its coordinates.
                int x = (int) e.getX(pointerIndex);
                int y = (int) e.getY(pointerIndex);

                var viewport = renderer.toViewport(x, y);
                int worldX = viewport.x;
                int worldY = viewport.y;

                var hudViewport = renderer.toHudViewport(x, y);
                int hudX = hudViewport.x;
                int hudY = hudViewport.y;

                // Check top elements first
                if (processPointerDown(e, pointerId, pointerIndex, hudX, hudY, renderer.toptouchelements)) {
                    return true;
                }
                // Then check world elements
                if (processPointerDown(e, pointerId, pointerIndex, worldX, worldY, renderer.touchelements)) {
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                boolean handled = false;
                // Iterate through all currently active pointers
                for (int i = 0; i < e.getPointerCount(); i++) {
                    int currentPointerId = e.getPointerId(i);
                    Element liftedElement = pointerIdToLiftedElementMap.get(currentPointerId);

                    if (liftedElement != null) {
                        int x = (int) e.getX(i);
                        int y = (int) e.getY(i);

                        boolean isHudElement = renderer.toptouchelements.contains(liftedElement);
                        int moveX, moveY;
                        if (isHudElement) {
                            var hudViewport = renderer.toHudViewport(x, y);
                            moveX = hudViewport.x;
                            moveY = hudViewport.y;
                        } else {
                            var viewport = renderer.toViewport(x, y);
                            moveX = viewport.x;
                            moveY = viewport.y;
                        }
                        liftedElement.touchDrag(moveX, moveY); // You might want to pass pointerId here too
                        handled = true;
                    }
                }
                return handled; // Return true if any pointer handled the move
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP: {
                // This pointer (identified by pointerId) has lifted.
                Element liftedElement = pointerIdToLiftedElementMap.get(pointerId);
                if (liftedElement != null) {
                    int x = (int) e.getX(pointerIndex);
                    int y = (int) e.getY(pointerIndex);

                    boolean isHudElement = renderer.toptouchelements.contains(liftedElement);
                    int upX, upY;
                    if (isHudElement) {
                        var hudViewport = renderer.toHudViewport(x, y);
                        upX = hudViewport.x;
                        upY = hudViewport.y;
                    } else {
                        var viewport = renderer.toViewport(x, y);
                        upX = viewport.x;
                        upY = viewport.y;
                    }

                    liftedElement.touchUp(upX, upY); // You might want to pass pointerId here too
                    pointerIdToLiftedElementMap.remove(pointerId);
                    return true; // This pointer's up event was handled
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                // Treat cancel like an UP for all pointers, or handle more gracefully
                for (int i = 0; i < pointerIdToLiftedElementMap.size(); i++) {
                    int currentPointerId = pointerIdToLiftedElementMap.keyAt(i);
                    Element liftedElement = pointerIdToLiftedElementMap.valueAt(i);
                    // Optionally, you might not have valid coordinates for ACTION_CANCEL
                    // Call a specific touchCancel method or reuse touchUp with last known coords
                    liftedElement.touchUp(-1, -1); // Or a specific cancel method
                }
                pointerIdToLiftedElementMap.clear();
                return true;
            }
        }
        // Return true if any pointer is still actively interacting
        return pointerIdToLiftedElementMap.size() > 0;
    }

    /**
     * Processes a touch down event for a specific pointer.
     *
     * @param e            The MotionEvent.
     * @param pointerId    The ID of the pointer that went down.
     * @param pointerIndex The index of the pointer in the MotionEvent.
     * @param x            The transformed x-coordinate for hit testing.
     * @param y            The transformed y-coordinate for hit testing.
     * @param elements     The list of elements to check.
     * @return True if an element was touched and interaction started, false otherwise.
     */
    private boolean processPointerDown(MotionEvent e, int pointerId, int pointerIndex, int x, int y, List<Element> elements) {
        // Iterate in reverse to prioritize elements drawn on top
        final List<Element> reversedElements = new ArrayList<>(elements);
        Collections.reverse(reversedElements);

        for (Element s : reversedElements) {
            if (s.isVisible() && s.contains(x, y)) {
                // Pass the pointerId to touchDown if your Element needs to track it
                if (s.touchDown( x, y /*, pointerId (optional) */)) {
                    pointerIdToLiftedElementMap.put(pointerId, s);
                    return true;
                }
            }
        }
        return false;
    }

    public GLRenderer getRenderer() {
        return renderer;
    }

    // onKeyDown remains the same
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}