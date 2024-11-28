package io.github.ngspace.topdownshooter;

import android.view.MotionEvent;

import io.github.ngspace.topdownshooter.engine.GameActivity;
import io.github.ngspace.topdownshooter.engine.opengl.elements.Bounds;
import io.github.ngspace.topdownshooter.engine.opengl.renderer.GLRenderer;
import io.github.ngspace.topdownshooter.engine.opengl.renderer.Textures;
import io.github.ngspace.topdownshooter.engine.opengl.elements.Sprite;

public class TestGameActivity extends GameActivity {

    Bounds memberbounds;
    Sprite p;

    @Override public void start(GLRenderer renderer) {
        p = new Sprite(Textures.SIMLEY, 0.1f, .9f, .5f, .5f) {};
        renderer.hudelements.add(p);
        p.addTouchDownListener((e, x, y) -> {
            memberbounds = p.getBounds();
            p.setBounds(x - p.getWidth()/2, y - p.getHeight()/2, .5f, .5f);
        });
        p.addTouchDragListener((e, x, y) -> p.setBounds(x - p.getWidth()/2, y - p.getHeight()/2, .5f, .5f));
        p.addTouchUpListener((e, x, y) -> p.setBounds(memberbounds));
    }

    @Override public void update(GLRenderer renderer, float delta) {

    }
}