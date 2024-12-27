package io.github.ngspace.topdownshooter.renderer.elements;

import android.opengl.GLES32;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import io.github.ngspace.topdownshooter.utils.Bounds;
import io.github.ngspace.topdownshooter.renderer.renderer.GLRenderer;
import io.github.ngspace.topdownshooter.renderer.renderer.Shaders;

public class PostProc extends Element {

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    public IntBuffer buffer = IntBuffer.allocate(1);
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 2;
    static float[] squareCoords = {
            -1.0f,  1.0f,   // top left
            -1.0f, -1.0f,   // bottom left
            1.0f, -1.0f,   // bottom right
            1.0f,  1.0f}; // top right

    private final short[] drawOrder = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    float[] color = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
     */
    public PostProc() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(/* (# of coordinate values * 2 bytes per short) */drawOrder.length * 2);

        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = GLRenderer.loadShader(GLES32.GL_VERTEX_SHADER, Shaders.POSTPROC_2D_VERT_SHADER);
        int fragmentShader = GLRenderer.loadShader(GLES32.GL_FRAGMENT_SHADER, Shaders.POSTPROC_2D_FRAG_SHADER);

        mProgram = GLES32.glCreateProgram();             // create empty OpenGL Program
        GLES32.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES32.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES32.glLinkProgram(mProgram);                  // create OpenGL program executables

        GLES32.glGenFramebuffers(1, buffer);
        GLES32.glBindFramebuffer(GLES32.GL_FRAMEBUFFER,buffer.get(0));
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
     */
    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES32.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES32.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES32.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES32.GL_FLOAT, false,
                COORDS_PER_VERTEX * 4, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES32.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES32.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES32.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES32.glDrawElements(
                GLES32.GL_TRIANGLES, drawOrder.length,
                GLES32.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES32.glDisableVertexAttribArray(mPositionHandle);
    }

    @Override public Bounds getBounds() {return new Bounds(0,0,2,2);}

    @Override public void setBounds(float x, float y, float width, float height) {
        throw new UnsupportedOperationException("Unused method, not necessary to fill.");
    }

    @Override public boolean touchDown(MotionEvent e, int x, int y) {return false;}
    @Override public void touchDrag(MotionEvent e, int x, int y) {}
    @Override public void touchUp  (MotionEvent e, int x, int y) {}

    public void preDraw(GLRenderer glRenderer) {

    }
}
