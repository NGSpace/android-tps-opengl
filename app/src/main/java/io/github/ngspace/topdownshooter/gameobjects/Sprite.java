package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.renderer.GameScene;
import io.github.ngspace.topdownshooter.renderer.opengl.Bounds;
import io.github.ngspace.topdownshooter.renderer.opengl.elements.Texture;
import io.github.ngspace.topdownshooter.renderer.opengl.renderer.TextureInfo;

public class Sprite extends AGameObject {
    protected final Texture texture;

    public Sprite(TextureInfo texture, float x, float y, float width, float height) {
        super(new Bounds(x,y,width,height));
        this.texture = new Texture(texture, x, y, width, height);
    }


    @Override public void init(GameScene scene) {super.init(scene);scene.getRenderer().addElement(texture);}

    @Override public void setBounds(Bounds bounds) {
        super  .setBounds(bounds);
        texture.setBounds(bounds);
    }
}
