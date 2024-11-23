/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ngspace.topdownshooter.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import io.github.ngspace.topdownshooter.opengl.elements.Shape;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class OpenGLSurfaceView extends GLSurfaceView {

    private final GLRenderer mRenderer;

    public OpenGLSurfaceView(Context context) {
        super(context);
        // Create an OpenGL ES 3.0 context.
        setEGLContextClientVersion(3);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer((mRenderer = new GLRenderer(this)));

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        var viewport = mRenderer.toViewport(x,y);
        x = viewport.x;
        y = viewport.y;
        if (e.getAction()==MotionEvent.ACTION_UP) {
            for (Shape s : mRenderer.elements) if (s.intersects(x,y)) s.touchUp(e, x, y);
            return true;
        }
        if (e.getAction()==MotionEvent.ACTION_MOVE) {
            for (Shape s : mRenderer.elements) {
                Log.i("NGSPACEly",x + "  " + y);
                if (s.intersects(x,y)) s.touchDrag(e, x, y);
            }
            return true;
        }
        if (e.getAction()==MotionEvent.ACTION_DOWN) {
            for (Shape s : mRenderer.elements) if (s.intersects(x,y)) s.touchDown(e, x, y);
            return true;
        }
        return false;
    }

}
