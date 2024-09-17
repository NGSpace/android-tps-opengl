package io.github.ngspace.topdownshooter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Sprite implements Shape {

    private final TextureInfo texture;
    public float angle = 0;

    //Added for Textures
    private FloatBuffer mCubeTextureCoordinates;
    private int mTextureUniformHandle;
    private int mTextureCoordinateHandle;

    private final String vertexShaderCode = """
attribute vec2 a_TexCoordinate;
varying vec2 v_TexCoordinate;
uniform mat4 uMVPMatrix;
attribute vec4 vPosition;
void main() {
    gl_Position = uMVPMatrix * vPosition;
    v_TexCoordinate = a_TexCoordinate;
}
""";

    private final String fragmentShaderCode =
//            "precision mediump float;" +
//                    "uniform float vAlpha;" +
//                    "uniform sampler2D u_Texture;" +
//                    "varying vec2 v_TexCoordinate;" +
//                    "void main() {" +
//                    "gl_FragColor = texture2D(u_Texture, v_TexCoordinate);" +
//                    "if (gl_FragColor.a == 0.0) {\n" +
//                    "    discard;\n" +
//                    "}\n" +
//                    "}";
"""

precision mediump float;
uniform float vAlpha;
uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;

void main() {
//    vec4 color = texture2D(u_Texture, v_TexCoordinate);
//    if (color.w < 1.0) {
//        gl_FragColor = vec4(1,0,0,1);
//    } else {
//        gl_FragColor = vec4(0,1,0,1);
//    }
//    gl_FragColor = texture2D(u_Texture, v_TexCoordinate);
//    if (gl_FragColor.a == 0.0) {
//        discard;
//    }

    vec4 texColor = texture2D(u_Texture, v_TexCoordinate);
//    if(texColor.a < 0.1) discard;
    gl_FragColor = texColor;
}
""";

    public float alpha = 1f;

    private final int shaderProgram;
    private FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    final int COORDS_PER_VERTEX = 2;
    float spriteCoords[] = { -0.5f,  0.5f,   // top left
            -0.5f, -0.5f,   // bottom left
            0.5f, -0.5f,   // bottom right
            0.5f,  0.5f }; //top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; //Order to draw vertices
    private final int vertexStride = COORDS_PER_VERTEX * 4; //Bytes per vertex

    public Sprite(TextureInfo texture, float x, float y, float width, float height) {
        bounds(x,y,width,height);

        //Initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(spriteCoords.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = MyGLRenderer.loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode);

        shaderProgram = GLES30.glCreateProgram();
        GLES30.glAttachShader(shaderProgram, vertexShader);
        GLES30.glAttachShader(shaderProgram, fragmentShader);

        //Texture Code
        GLES30.glBindAttribLocation(shaderProgram, 0, "a_TexCoordinate");
        GLES30.glLinkProgram(shaderProgram);

        //Load the texture
        this.texture = texture;

        mCubeTextureCoordinates = ByteBuffer.allocateDirect(texture.textureCoordinate.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mCubeTextureCoordinates.put(texture.textureCoordinate).position(0);
    }
    float x, y, width, height;

    float velx = 0;

    public void draw(float[] mvpMatrix) {
//
        this.x = this.x + velx;
        if (velx>0) {
            velx-=.1;
            if (velx<0) velx = 0;
        } else {
            velx+=.1;
            if (velx>0) velx = 0;
        }

        bounds(this.x, this.y, width, height);

        final float[] mRotationMatrix = new float[16];
        float[] scratch = new float[16];

        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, 1.0f);

        Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, mRotationMatrix, 0);

        //Add program to OpenGL ES Environment
        GLES30.glUseProgram(shaderProgram);

        //Get handle to vertex shader's vPosition member
        mPositionHandle = GLES30.glGetAttribLocation(shaderProgram, "vPosition");

        //Enable a handle to the triangle vertices
        GLES30.glEnableVertexAttribArray(mPositionHandle);

        //Prepare the triangle coordinate data
        GLES30.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES30.GL_FLOAT, false, vertexStride, vertexBuffer);

        //Get Handle to Fragment Shader's vColor member
        mColorHandle = GLES30.glGetUniformLocation(shaderProgram, "vAlpha");

        //Set the Color for drawing the triangle
        GLES30.glUniform1f(mColorHandle, alpha);

        //Set Texture Handles and bind Texture
        mTextureUniformHandle = GLES30.glGetAttribLocation(shaderProgram, "u_Texture");
        mTextureCoordinateHandle = GLES30.glGetAttribLocation(shaderProgram, "a_TexCoordinate");

        //Set the active texture unit to texture unit 0.
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);

        //Bind the texture to this unit.
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texture.textureDataHandle);

        //Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES30.glUniform1i(mTextureUniformHandle, 0);

        //Pass in the texture coordinate information
        mCubeTextureCoordinates.position(0);
        GLES30.glVertexAttribPointer(mTextureCoordinateHandle, 2, GLES30.GL_FLOAT, false, 0, mCubeTextureCoordinates);
        GLES30.glEnableVertexAttribArray(mTextureCoordinateHandle);

        //Get Handle to Shape's Transformation Matrix
        mMVPMatrixHandle = GLES30.glGetUniformLocation(shaderProgram, "uMVPMatrix");

        //Apply the projection and view transformation
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, scratch, 0);

        //Draw the triangle
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, drawOrder.length, GLES30.GL_UNSIGNED_SHORT, drawListBuffer);

        //Disable Vertex Array
        GLES30.glDisableVertexAttribArray(mPositionHandle);
    }

    float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public void touchDown(float x, float y) {

    }
    @Override public void touchDrag(float x, float y) {
        float clickx = (x/OpenGLActivity.realWidth*4+0.125f)-1f;
        float clicky = (y/OpenGLActivity.realHeight*2);

//        bounds(x/OpenGLActivity.realWidth*4-0.125f      ,y/OpenGLActivity.realHeight*2-.25f, .5f, .5f);
//        float hyp = (float) Math.sqrt(Math.pow(this.x-clickx,2) + Math.pow(this.y-clicky,2));

//        angle = (float) Math.acos(hyp);

        velx += clamp(clickx/20, -5, 5);
        Log.i("NGSPACEly", "   " + clickx + "   " + clicky + "   " + this.x + "   " + this.y + "   ");

//        bounds(newx, this.y, width, height);
    }

    @Override
    public void touchUp(float x, float y) {

    }

    public void bounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        float realx = 2f - x;
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
}