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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.PointF;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.ngspace.topdownshooter.opengl.elements.Shape;
import io.github.ngspace.topdownshooter.opengl.elements.Sprite;
import io.github.ngspace.topdownshooter.opengl.elements.Square;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class GLRenderer implements android.opengl.GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
//    public Sprite smiley;
//    public Sprite background;

    List<Shape> elements = new ArrayList<Shape>();

    OpenGLSurfaceView context;
    public Camera camera = new Camera();
    public Shape background;

    public GLRenderer(OpenGLSurfaceView OpenGLSurfaceView) {
        this.context = OpenGLSurfaceView;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        background = new Square();

        //elements.add(new Square(context.getContext()));
        elements.add(new Sprite(Textures.STARSET, 0f, 0f, 2f, 2f, this) {
            @Override public void touchDrag(MotionEvent e, float x, float y) {}
            @Override public void touchDown(MotionEvent e, float x, float y) {}
            @Override public void touchUp(MotionEvent e, float x, float y) {}
        });
        elements.add(new Sprite(Textures.ANCHOR, 0f, 0f, 2f, 2f, this));//2-.25f, 1-.25f,.5f, .5f));
//        elements.add(new Sprite(Textures.SIMLEY, 1-(.75f/2), 2-1.25f, .75f, 1.25f));
//        elements.add(new Sprite(Textures.FEDORA, (0.45f/2)-.1f, 2-0.95f, 0.45f, 0.95f));
        elements.add(new Sprite(Textures.FUCKOPENGL, 2-.55f, 2-0.95f, 0.45f, 0.95f, this));
//        ((Sprite) elements.get(0)).bounds(1,1,1,1);
    }

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] backgroundMatrix = {-3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.5f, 1.0f, 0.0f, 0.0f, -3.0f, 3.0f};

    @Override
    public void onDrawFrame(GL10 unused) {

        // Draw background color
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, -0, 0f, -3f, .0f, 0.0f, 0f, .0f, 1.0f, 0.0f);
        var v = new float[16];

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, camera.getProjectionMatrix(), 0, mViewMatrix, 0);

        // Draw background
        background.draw(backgroundMatrix);
        // Draw elements
        for (Shape shape : elements) shape.draw(mMVPMatrix);
    }

    int viewportBuffer;

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        float height1 = context.getHeight();
        float relation = height1/1080;
        viewportBuffer = (int) (context.getWidth() - (1920*relation));
        GLES30.glViewport(viewportBuffer/2,0, (int) (1920*relation), (int) (1080*relation));

        camera.updateProjection();
    }

    public PointF toViewport(float x, float y) {
        return new PointF(((x-viewportBuffer/2f)/(OpenGLActivity.realWidth-viewportBuffer))*2, (y/OpenGLActivity.realHeight)*2);
    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

}