package io.github.ngspace.topdownshooter.engine.opengl;

import android.view.MotionEvent;

@FunctionalInterface public interface TouchEventListener {
    public void exec(MotionEvent e, float x, float y);
}
