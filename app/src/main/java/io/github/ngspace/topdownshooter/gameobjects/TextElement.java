package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.renderer.elements.Text;
import io.github.ngspace.topdownshooter.utils.Bounds;

public class TextElement extends AGameObject {
    protected final Text text;

    public TextElement(String text, float x, float y, int fontsize, float height) {
        super(new Bounds(x,y,0,height));
        this.text = new Text(text, x, y, fontsize, height);
    }

    public TextElement(String text, Bounds bounds, int fontsize) {
        this(text, bounds.x(), bounds.y(), fontsize, bounds.height());
    }


    @Override public void init(GameScene scene) {super.init(scene);scene.getRenderer().addElement(text);}
    @Override public void destroy(GameScene scene) {super.destroy(scene);scene.getRenderer().removeElement(text);}

    @Override public void setBounds(Bounds bounds) {super.setBounds(bounds);text.setBounds(bounds);}
    @Override public void setAngle(float angle) {super.setAngle(angle);text.setAngle(angle);}
    @Override public void setVisible(boolean visible) {super.setVisible(visible);text.setVisible(visible);}

    public void setText(String s) {
        text.setText(s);
    }
    public String getText() {
        return text.getText();
    }
}
