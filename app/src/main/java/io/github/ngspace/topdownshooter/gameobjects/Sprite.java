package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.utils.Bounds;
import io.github.ngspace.topdownshooter.renderer.elements.Texture;
import io.github.ngspace.topdownshooter.renderer.renderer.TextureInfo;

public class Sprite extends AGameObject {
    protected final Texture texture;

    public Sprite(TextureInfo texture, float x, float y, float width, float height) {
        super(new Bounds(x,y,width,height));
        this.texture = new Texture(texture, x, y, width, height);
    }

    public Sprite(TextureInfo texture, Bounds bounds) {
        this(texture, bounds.x(),bounds.y(),bounds.width(),bounds.height());
    }


    @Override public void init(GameScene scene) {super.init(scene);scene.getRenderer().addElement(texture);}
    @Override public void destroy(GameScene scene) {super.destroy(scene);scene.getRenderer().removeElement(texture);}

    @Override public void setBounds(Bounds bounds) {super.setBounds(bounds);texture.setBounds(bounds);}
    @Override public void setAngle(float angle) {super.setAngle(angle);texture.setAngle(angle);}
    @Override public void setVisible(boolean visible) {super.setVisible(visible);texture.setVisible(visible);}

    public void setAlpha(float alpha) {texture.setAlpha(alpha);}
    public float getAlpha() {return texture.getAlpha();}

    public TextureInfo getTextureInfo() {
        return texture.getTexture();
    }
}
