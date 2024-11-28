package io.github.ngspace.topdownshooter.renderer.opengl;

import io.github.ngspace.topdownshooter.renderer.opengl.elements.Shape;

@FunctionalInterface public interface TouchEventListener {
    public void exec(Shape e, int x, int y);
}
