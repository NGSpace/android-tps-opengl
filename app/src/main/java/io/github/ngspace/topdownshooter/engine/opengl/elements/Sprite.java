package io.github.ngspace.topdownshooter.engine.opengl.elements;

import android.graphics.RectF;
import android.opengl.GLES32;
import android.opengl.Matrix;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import io.github.ngspace.topdownshooter.engine.opengl.GLRenderer;
import io.github.ngspace.topdownshooter.engine.opengl.Shaders;
import io.github.ngspace.topdownshooter.engine.opengl.TextureInfo;

public class Sprite extends Shape {

    private TextureInfo texture;
    private GLRenderer renderer;
    private float angle = 0;

    //Added for Textures
    private FloatBuffer mCubeTextureCoordinates;
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;

    private float alpha = 1f;

    private final int shaderProgram;
    private FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;

    // number of coordinates per vertex in this array
    final int COORDS_PER_VERTEX = 2;
    float[] spriteCoords = { -0.5f,  0.5f,   // top left
            -0.5f, -0.5f,   // bottom left
            0.5f, -0.5f,   // bottom right
            0.5f,  0.5f }; //top right

    private final short[] drawOrder = { 0, 1, 2, 0, 2, 3 }; //Order to draw vertices
    private final int vertexStride = COORDS_PER_VERTEX * 4; //Bytes per vertex

    private float x;
    private float y;
    private float width;
    private float height;
    boolean ispressed = false;

    public Sprite(TextureInfo texture, float x, float y, float width, float height, GLRenderer renderer) {
        this.texture = texture;
        this.renderer = renderer;
        setBounds(x,y,width,height);

        //Initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(spriteCoords.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = GLRenderer.loadShader(GLES32.GL_VERTEX_SHADER, Shaders.TEXTURE_2D_VERT_SHADER);
        int fragmentShader = GLRenderer.loadShader(GLES32.GL_FRAGMENT_SHADER, Shaders.TEXTURE_2D_FRAG_SHADER);

        shaderProgram = GLES32.glCreateProgram();
        GLES32.glAttachShader(shaderProgram, vertexShader);
        GLES32.glAttachShader(shaderProgram, fragmentShader);

        //Texture Code
        GLES32.glBindAttribLocation(shaderProgram, 0, "a_TexCoordinate");
        GLES32.glLinkProgram(shaderProgram);

        mCubeTextureCoordinates = ByteBuffer.allocateDirect(texture.textureCoordinate.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeTextureCoordinates.put(texture.textureCoordinate).position(0);
    }

    public void draw(float[] mvpMatrix) {

        final float[] mRotationMatrix = new float[16];
        float[] scratch = new float[16];

        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, 1.0f);

        Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, mRotationMatrix, 0);

        //Add program to OpenGL ES Environment
        GLES32.glUseProgram(shaderProgram);

        //Get handle to vertex shader's vPosition member
        int mPositionHandle = GLES32.glGetAttribLocation(shaderProgram, "vPosition");

        //Enable a handle to the triangle vertices
        GLES32.glEnableVertexAttribArray(mPositionHandle);

        //Prepare the triangle coordinate data
        GLES32.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES32.GL_FLOAT, false, vertexStride, vertexBuffer);

        //Get Handle to Fragment Shader's vColor member
        int mColorHandle = GLES32.glGetUniformLocation(shaderProgram, "vAlpha");

        //Set the Color for drawing the triangle
        GLES32.glUniform1f(mColorHandle, alpha);

        //Set Texture Handles and bind Texture
        mTextureUniformHandle = GLES32.glGetAttribLocation(shaderProgram, "u_Texture");
        mTextureCoordinateHandle = GLES32.glGetAttribLocation(shaderProgram, "a_TexCoordinate");

        //Set the active texture unit to texture unit 0.
        GLES32.glActiveTexture(GLES32.GL_TEXTURE0);

        //Bind the texture to this unit.
        GLES32.glBindTexture(GLES32.GL_TEXTURE_2D, texture.textureDataHandle);

        //Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES32.glUniform1i(mTextureUniformHandle, 0);

        //Pass in the texture coordinate information
        mCubeTextureCoordinates.position(0);
        GLES32.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES32.GL_FLOAT, false, 0, mCubeTextureCoordinates);
        GLES32.glEnableVertexAttribArray(mTextureCoordinateHandle);

        //Get Handle to Shape's Transformation Matrix
        int mMVPMatrixHandle = GLES32.glGetUniformLocation(shaderProgram, "uMVPMatrix");

        //Apply the projection and view transformation
        GLES32.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, scratch, 0);

        //Draw the triangle
        GLES32.glDrawElements(GLES32.GL_TRIANGLES, drawOrder.length, GLES32.GL_UNSIGNED_SHORT, drawListBuffer);

        //Disable Vertex Array
        GLES32.glDisableVertexAttribArray(mPositionHandle);
    }

    @Override
    public RectF getBounds() {return new RectF(x, y, x+width, y+height);}

    public void setBounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        float realx = 1f - x;
        float realy = 1f - y;
        spriteCoords = new float[] {realx, realy-height,   // top left
                realx-width, realy-height,   // bottom left
                realx-width, realy,   // bottom right
                realx, realy}; //top right
        //Initialize Vertex Byte Buffer for Shape Coordinates / # of coordinate values * 4 bytes per float
        ByteBuffer bb = ByteBuffer.allocateDirect(spriteCoords.length * 4);
        //Use the Device's Native Byte Order
        bb.order(ByteOrder.nativeOrder());
        //Create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        //Add the coordinates to the FloatBuffer
        vertexBuffer.put(spriteCoords);
        //Set the Buffer to Read the first coordinate
        vertexBuffer.position(0);
    }

    @Override public boolean touchUp(MotionEvent e, float x, float y) {
        ispressed = false;
        float xpos = 0;
        float ypos = 0;
        if (1.25>x&&x>.75) xpos = .5f; else if (x>1.25) xpos = 1f;
        if (1.25>y&&y>.75) ypos = .5f; else if (y>1.25) ypos = 1f;
        setBounds(xpos,ypos,width,height);
        return true;
    }
    @Override public boolean touchDown(MotionEvent e, float x, float y) {
        setBounds(x-.5f,y-.5f,1,1);
        renderer.addExec(r->r.elements.remove(this));
        renderer.addExec(r->r.elements.add(this));
        ispressed = true;
        return true;
    }
    @Override public boolean touchDrag(MotionEvent e, float x, float y) {
        //renderer.camera.setProjection(x,y);
        setBounds(x-.5f,y-.5f,1,1);
        return true;
    }



    public float getX() {return x;}
    public float getY() {return y;}
    public float getWidth() {return width;}
    public float getHeight() {return height;}

    public float getAlpha() {return alpha;}
    public float getAngle() {return angle;}

    public TextureInfo getTexture() {return texture;}
    public GLRenderer getRenderer() {return renderer;}
    public int getShaderProgram() {return shaderProgram;}
    public boolean isPressed() {return ispressed;}


    // Setters
    public void setTexture(TextureInfo texture) {this.texture = texture;}
    public void setAlpha(float alpha) {this.alpha = alpha;}
    public void setAngle(float angle) {this.angle = angle;}
    public void setSpriteCoords(float[] spriteCoords) {this.spriteCoords = spriteCoords;}
}