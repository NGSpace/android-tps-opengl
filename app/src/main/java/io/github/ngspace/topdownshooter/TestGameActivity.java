package io.github.ngspace.topdownshooter;

import io.github.ngspace.topdownshooter.engine.GameActivity;
import io.github.ngspace.topdownshooter.engine.opengl.GLRenderer;
import io.github.ngspace.topdownshooter.engine.opengl.Textures;
import io.github.ngspace.topdownshooter.engine.opengl.elements.Sprite;

public class TestGameActivity extends GameActivity {

    @Override public void start(GLRenderer renderer) {
//        elements.add(new Sprite(Textures.STARSET, 0f, 0f, 2f, 2f, this) {
//            @Override public boolean touchDrag(MotionEvent e, float x, float y) {return false;}
//            @Override public boolean touchDown(MotionEvent e, float x, float y) {return false;}
//            @Override public boolean touchUp(MotionEvent e, float x, float y) {return false;}
//        });
//        renderer.elements.add(new Sprite(Textures.FUCKOPENGL, 0f, 0f, 1f, 1f, renderer));
//        renderer.elements.add(new Sprite(Textures.SIMLEY, 0f, 1f, 1f, 1f, renderer));
//        renderer.elements.add(new Sprite(Textures.FEDORA, 1f, 0f, 1f, 1f, renderer));
//        renderer.elements.add(new Sprite(Textures.STARSET, 1f, 1f, 1f, 1f, renderer));
    }

    @Override public void update(GLRenderer renderer, float delta) {

    }
}