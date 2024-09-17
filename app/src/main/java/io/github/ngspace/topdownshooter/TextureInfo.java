package io.github.ngspace.topdownshooter;

public class TextureInfo {
    public float[] textureCoordinate = new float[]{
            0f, 1f,
            1f, 1f,
            1f, 0f,
            0f, 0f,
    };
    public int textureDataHandle = 1;

    TextureInfo(int textureDataHandle) {
        this.textureDataHandle = textureDataHandle;
    }

//        float fl = 800/Atlas.width;
//        float st = ((atlasId)/(Atlas.length));
//        float[] cubeTextureCoordinateData = {
//                st, 1f,
//                st+fl, 1f,
//                st+fl, 0f,
//                st, 0f,
//        };
}
