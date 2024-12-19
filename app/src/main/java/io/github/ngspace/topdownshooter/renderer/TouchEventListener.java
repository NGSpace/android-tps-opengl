package io.github.ngspace.topdownshooter.renderer;

import io.github.ngspace.topdownshooter.renderer.elements.Shape;

@FunctionalInterface public interface TouchEventListener {
    public void exec(Shape e, int x, int y);
}
