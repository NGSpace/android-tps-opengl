package io.github.ngspace.topdownshooter.renderer;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.github.ngspace.topdownshooter.physics.PhysicsManager;
import io.github.ngspace.topdownshooter.renderer.opengl.OpenGLActivity;
import io.github.ngspace.topdownshooter.gameobjects.AGameObject;

public abstract class GameScene extends OpenGLActivity {

    private PhysicsManager manager = new PhysicsManager();
    private List<AGameObject> gameObjects = new ArrayList<AGameObject>();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer.setCreationListener(r->start());
        renderer.addDrawListener((r,d)->{manager.update();update(d);});
    }

    public abstract void start();
    public abstract void update(float delta);

    public void addObject(AGameObject object) {
        gameObjects.add(object);
        object.init(this);
    }
    public void addCollidableObject(AGameObject object) {
        addObject(object);
        manager.addObject(object);
    }
}
