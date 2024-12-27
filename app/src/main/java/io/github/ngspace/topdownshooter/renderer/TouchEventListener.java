package io.github.ngspace.topdownshooter.renderer;

import io.github.ngspace.topdownshooter.renderer.elements.Element;

@FunctionalInterface public interface TouchEventListener {
    public void exec(Element e, int x, int y);
}
