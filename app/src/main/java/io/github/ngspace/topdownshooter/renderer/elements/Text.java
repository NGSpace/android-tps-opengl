package io.github.ngspace.topdownshooter.renderer.elements;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import io.github.ngspace.topdownshooter.renderer.renderer.TextureInfo;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;
import io.github.ngspace.topdownshooter.utils.Bounds;

public class Text extends Element {

    private float height;
    private String text = "Filler text";
    private int fontwidth = 100;
    float x;
    float y;
    Char[] chars;

    public Text(String text, float x, float y, int fontwidth, float height) {
        this.fontwidth = fontwidth;
        this.height = height;
        this.x = x;
        this.y = y;
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        chars = new Char[text.length()];
        for (int i = 0; i < text.length(); i++) {
            chars[i] = new Char(x + i * (fontwidth*.5f), y, fontwidth, height, text.charAt(i));
        }
    }
    public String getText() {
        return text;
    }

    public void setFontwidth(int fontwidth) {
        this.fontwidth = fontwidth;
        setText(getText());
    }
    public int getFontwidth() {
        return fontwidth;
    }

    public void setHeight(float height) {
        this.height = height;
        setText(getText());
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void draw(float[] mvpMatrix) {
        for (Char c : chars) {
            c.draw(mvpMatrix);
        }
    }

    @Override
    public Bounds getBounds() {
        return new Bounds(x, y, fontwidth * text.length(), fontwidth);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
    }

    static class Char extends Texture {
        static final float CHAR_WIDTH = 60;
        static final float SPACE_BETWEEN = CHAR_WIDTH/5640;
        public Char(float x, float y, float fontwidth, float height, char c) {
            super(Textures.TEXT, x, y, fontwidth*.8f, height);

            float charpos = c-32f;
            float st = charpos*SPACE_BETWEEN;

            textureCoordinate = new float[] {
                    st, 1f,
                    st+SPACE_BETWEEN, 1f,
                    st+SPACE_BETWEEN, 0f,
                    st, 0f,
            };

            mCubeTextureCoordinates = ByteBuffer.allocateDirect(textureCoordinate.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            mCubeTextureCoordinates.put(textureCoordinate).position(0);
        }
    }
}
