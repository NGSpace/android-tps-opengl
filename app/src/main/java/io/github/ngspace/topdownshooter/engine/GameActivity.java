package io.github.ngspace.topdownshooter.engine;

import android.os.Bundle;

import io.github.ngspace.topdownshooter.engine.opengl.GLRenderer;
import io.github.ngspace.topdownshooter.engine.opengl.OpenGLActivity;

public abstract class GameActivity extends OpenGLActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer.setCreationListener(this::start);
        renderer.addDrawListener(this::update);
    }

    public abstract void start(GLRenderer renderer);
    public abstract void update(GLRenderer renderer, float delta);
}
