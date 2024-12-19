package io.github.ngspace.topdownshooter.engine;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.renderer.OpenGLActivity;

public abstract class GameScene extends OpenGLActivity {

    private PhysicsManager physicsManager = new PhysicsManager();
    private List<AGameObject> gameObjects = new ArrayList<AGameObject>();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer.setCreationListener(r->start());
        renderer.addDrawListener((r,d)->{physicsManager.update();update(d);});
    }

    public abstract void start();
    public abstract void update(float delta);

    public void addObject(AGameObject object) {
        if (gameObjects.contains(object)) return;
        gameObjects.add(object);
        object.init(this);
    }
    public void addCollidableObject(AGameObject object) {
        addObject(object);
        physicsManager.addObject(object);
    }
}
