package io.github.ngspace.topdownshooter.engine.opengl;

import io.github.ngspace.topdownshooter.engine.opengl.elements.Shape;

@FunctionalInterface public interface TouchEventListener {
    public void exec(Shape e, float x, float y);
}
