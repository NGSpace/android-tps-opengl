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
package io.github.ngspace.topdownshooter.opengl.elements;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.content.res.Resources;
import android.graphics.RectF;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import io.github.ngspace.topdownshooter.opengl.GLRenderer;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 3.1
 *
 * actually fails with the 300.  I do not have a clue what it doesn't work.
 */
public class Triangle implements Shape {

    private final String vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "#version 310 es\n"+
        "uniform mat4 uMVPMatrix;\n" +
            "in vec4 vPosition;\n" +
            "void main() {\n" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;\n" +
            "}\n";

    private final String fragmentShaderCode =
        "#version 310 es \n"+
        "precision mediump float; \n" +
        "in uniform vec4 vColor;\n" +
        "out vec4 gl_FragColor;\n" +
        "void main() {\n" +
        "  gl_FragColor = vColor;\n" +
        "}\n";

    private final FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {
        // in counterclockwise order:
        0.0f, 0.622008459f, 0.0f,   // top
        -0.5f, -0.311004243f, 0.0f,   // bottom left
        0.5f, -0.311004243f, 0.0f    // bottom right
    };
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 0.0f};

    float angle = 0;

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public Triangle() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
            // (number of coordinate values * 4 bytes per float)
            triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = GLRenderer.loadShader(
            GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = GLRenderer.loadShader(
            GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES30.glCreateProgram();             // create empty OpenGL Program
        GLES30.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES30.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES30.glLinkProgram(mProgram);                  // create OpenGL program executables

    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     *                  this shape.
     */
    public void draw(float[] mvpMatrix) {

        final float[] mRotationMatrix = new float[16];
        float[] scratch = new float[16];


        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, 1.0f);

        Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, mRotationMatrix, 0);

        // Add program to OpenGL environment
        GLES30.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES30.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES30.glVertexAttribPointer(
            mPositionHandle, COORDS_PER_VERTEX,
            GLES30.GL_FLOAT, false,
            vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES30.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, scratch, 0);
        GLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the triangle
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES30.glDisableVertexAttribArray(mPositionHandle);
    }

    @Override
    public RectF getBounds() {
        return new RectF(0,0,0,0);
    }

    @Override
    public void touchDown(float x, float y) {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
//        Log.i("NGSpace", ""+)
        float spacex = ((x/width)*2 );
        float spacey = ((y/height)*2);
        float hyp = (float) Math.sqrt(Math.pow(spacex,2) + Math.pow(spacey,2));

//        Log.i("NGSpace", ""+spacex);
//        Log.i("NGSpace", ""+spacey);
        Log.i("NGSpace", ""+Math.acos(spacey/hyp));
//        Log.i("NGSpace", ""+spacex/hyp);

        angle = (float) (Math.acos(spacey/hyp)/Math.PI) * 360;
//        angle%=360;

        Log.i("NGSpace", ""+angle);
//      Log.i("NGSpace", ""+height);
    }

    @Override
    public void touchDrag(float x, float y) {

    }

    @Override
    public void touchUp(float x, float y) {

    }
}