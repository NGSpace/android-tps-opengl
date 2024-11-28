package io.github.ngspace.topdownshooter.engine.opengl.renderer;

public class TextureInfo {
    public float[] textureCoordinate = new float[]{
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f,
    };
    public int textureDataHandle = 1;

    TextureInfo(int textureDataHandle) {this.textureDataHandle = textureDataHandle;}
}
