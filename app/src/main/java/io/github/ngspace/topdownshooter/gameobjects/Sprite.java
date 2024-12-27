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


    @Override public void init(GameScene scene) {super.init(scene);scene.getRenderer().addElement(texture);}
    @Override public void destroy(GameScene scene) {super.destroy(scene);scene.getRenderer().removeElement(texture);}

    @Override public void setBounds(Bounds bounds) {super.setBounds(bounds);texture.setBounds(bounds);}
    @Override public void setAngle(float angle) {super.setAngle(angle);texture.setAngle(angle);}
    @Override public void setHidden(boolean hidden) {super.setHidden(hidden);texture.setHidden(hidden);}
}
