package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.renderer.GameScene;
import io.github.ngspace.topdownshooter.renderer.opengl.Bounds;
import io.github.ngspace.topdownshooter.renderer.opengl.renderer.Textures;

public class JoyStick extends HudSprite {
    static final int SIZE = 200;
    static final int SPACE_FROM_CORNER = 150;
    static final int MAX_DISTANCE = 50;
    static final int YPOS = 1080-SIZE-SPACE_FROM_CORNER;
    Bounds initPos;
    public float xvel = 0;
    public float yvel = 0;
    public JoyStick() {
        super(Textures.STARSET, SPACE_FROM_CORNER, YPOS, SIZE, SIZE);
    }

    @Override
    public void init(GameScene scene) {
        super.init(scene);
        initPos = getBounds();
        addTouchDownListener((e, x, y) -> updatePosition(x,y));
        addTouchDragListener((e, x, y) -> updatePosition(x,y));
        addTouchUpListener  ((e, x, y) -> {setBounds(initPos);yvel=0;xvel=0;});
    }

    private void updatePosition(int x, int y) {
        float finalx = x - getWidth()/2;
        float finaly = y - getHeight()/2;
        finalx = clamp(finalx,SPACE_FROM_CORNER-MAX_DISTANCE,SPACE_FROM_CORNER+MAX_DISTANCE);
        finaly = clamp(finaly,YPOS-MAX_DISTANCE,YPOS+MAX_DISTANCE);
        xvel = (finalx-(SPACE_FROM_CORNER-MAX_DISTANCE))/50f - 1f;
        yvel = (finaly-(YPOS-MAX_DISTANCE))/50f - 1f;
        setBounds(finalx, finaly, initPos.width(), initPos.height());
    }
    float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}