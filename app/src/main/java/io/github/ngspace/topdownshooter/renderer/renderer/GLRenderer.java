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
package io.github.ngspace.topdownshooter.renderer.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.graphics.Point;
import android.opengl.GLES32;
import android.opengl.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import io.github.ngspace.topdownshooter.renderer.OpenGLSurfaceView;
import io.github.ngspace.topdownshooter.renderer.elements.PostProc;
import io.github.ngspace.topdownshooter.renderer.elements.Shape;
import io.github.ngspace.topdownshooter.renderer.elements.Texture;

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

    public List<Shape> elements = new ArrayList<Shape>();
    public List<Shape> topelements = new ArrayList<Shape>();
    public List<Shape> touchelements = new ArrayList<Shape>();
    public List<Shape> toptouchelements = new ArrayList<Shape>();
    public List<Consumer<GLRenderer>> Exec = new ArrayList<>();
    public List<BiConsumer<GLRenderer, Float>> drawListeners = new ArrayList<>();

    OpenGLSurfaceView context;
    public Camera camera = new Camera();
    public Shape background;
    public PostProc postProcessing;

    private float lastExecMs = 0;
    private Consumer<GLRenderer> creationListener;

    public GLRenderer(OpenGLSurfaceView OpenGLSurfaceView) {this.context = OpenGLSurfaceView;}

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES32.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Textures.loadTextures(context.getContext());
        background = new Texture(Textures.STARSET,0,0,1920,1080);

        creationListener.accept(this);
    }

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] hudMatrix = {-3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 3.0f, 0.0f, 0.0f, 0.0f, 0.0f, 2.5f, 1.0f, 0.0f, 0.0f, -3.0f, 3.0f};

    @Override public void onDrawFrame(GL10 unused) {
        float delta = 0f;
        if (lastExecMs>0) {
            delta = System.currentTimeMillis() - lastExecMs;
        }
        lastExecMs = System.currentTimeMillis();
        for (var v : Exec) v.accept(this);
        Exec.clear();

        camera.update();

        for (var v : drawListeners) v.accept(this, delta);

        // Draw background color
        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT | GLES32.GL_DEPTH_BUFFER_BIT | GLES32.GL_STENCIL_BUFFER_BIT);

        GLES32.glEnable(GLES32.GL_BLEND);
        GLES32.glBlendFunc(GLES32.GL_SRC_ALPHA, GLES32.GL_ONE_MINUS_SRC_ALPHA);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, -0, 0f, -3f, .0f, 0.0f, 0f, .0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, camera.getProjectionMatrix(), 0, mViewMatrix, 0);
        Matrix.multiplyMM(hudMatrix, 0, camera.getHudProjectionMatrix(), 0, mViewMatrix, 0);

        // Draw background
        background.render(mMVPMatrix);//TODO change this back to hud
        // Draw elements
        for (Shape shape : elements) shape.render(Arrays.copyOf(mMVPMatrix,mMVPMatrix.length));
        // Draw Post-Processing effects
        if (postProcessing!=null) postProcessing.render(hudMatrix);
        // Draw Hud
        for (Shape shape : topelements) shape.render(Arrays.copyOf(hudMatrix,hudMatrix.length));
    }


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        camera.updateViewport(context);
        camera.fixProjection();
    }

    public Point toViewport(int x, int y) {return camera.toViewport(x,y);}
    public Point toHudViewport(int x, int y) {return camera.toHudViewport(x,y);}

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type Vertex or fragment shader type.
     * @param shaderCode String containing the shader code.
     * @return - Returns an id for the shader.
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES32.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES32.GL_FRAGMENT_SHADER)
        int shader = GLES32.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES32.glShaderSource(shader, shaderCode);
        GLES32.glCompileShader(shader);

        return shader;
    }

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES32.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES32.glGetError()) != GLES32.GL_NO_ERROR) {
            Log.e("NGSPACEly", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    public synchronized void addExec(Consumer<GLRenderer> render) {Exec.add(render);}
    public synchronized void addDrawListener(BiConsumer<GLRenderer, Float> render) {drawListeners.add(render);}
    public synchronized void addElement(Shape element) {elements.add(element);}
    public synchronized void addHudElement(Shape element) {
        topelements.add(element);}

    public void setCreationListener(Consumer<GLRenderer> start) {this.creationListener = start;}

    public void addTouchElement(Shape shape) {touchelements.add(shape);}
    public void addHudTouchElement(Shape shape) {
        toptouchelements.add(shape);}
}