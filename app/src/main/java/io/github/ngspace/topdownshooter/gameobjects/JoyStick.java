package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.engine.GameScene;
import io.github.ngspace.topdownshooter.renderer.elements.Texture;
import io.github.ngspace.topdownshooter.utils.Bounds;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;

public class JoyStick extends HudSprite {
    static int SIZE = 200;
    static int SPACE_FROM_CORNER = 150;
    static int MAX_DISTANCE = 50;
    public int centerx;
    public int centery;
    Bounds initPos;
    public float xvel = 0;
    public float yvel = 0;
    public JoyStick(int posx, int posy) {
        super(Textures.GRAYCIRCLE, posx, posy, SIZE, SIZE);
        this.centerx=posx;
        this.centery=posy;
    }

    @Override public void init(GameScene scene) {
        super.init(scene);
        scene.getRenderer().addHudElement(new Texture(Textures.ANCHOR, getX(), getY(), getWidth(), getHeight()));
        initPos = getBounds();
        addTouchDownListener((e, x, y) -> updatePosition(x,y));
        addTouchDragListener((e, x, y) -> updatePosition(x,y));
        addTouchUpListener  ((e, x, y) -> {setBounds(initPos);yvel=0;xvel=0;});
    }

    private void updatePosition(float x, float y) {
        float finalx = x - getWidth()/2f;
        float finaly = y - getHeight()/2f;
        finalx = clamp(finalx,centerx-MAX_DISTANCE,centerx+MAX_DISTANCE);
        finaly = clamp(finaly,centery-MAX_DISTANCE,centery+MAX_DISTANCE);
        xvel = (finalx-(centerx-MAX_DISTANCE))/50f - 1f;
        yvel = (finaly-(centery-MAX_DISTANCE))/50f - 1f;
        setBounds(finalx, finaly, initPos.width(), initPos.height());
    }
    float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public float getStickAngle() {
        if (xvel==0&&yvel==0) return -1;
        return (theta(1920-SPACE_FROM_CORNER-50-200,1080-200-SPACE_FROM_CORNER, getX(), getY())-360)*-1+90f;
    }
    private float theta(float cx, float cy, float x, float y) {
        float angle = (float) (Math.toDegrees(Math.atan2(cy - y, x - cx)) + 90f);
        return (angle <= 180? angle: angle - 360)+180;
    }
}