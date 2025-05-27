package io.github.ngspace.topdownshooter.engine;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.renderer.OpenGLActivity;
import io.github.ngspace.topdownshooter.utils.Logcat;

public abstract class GameScene extends OpenGLActivity {

    private PhysicsManager physicsManager = new PhysicsManager();
    private List<AGameObject> gameObjects = new ArrayList<AGameObject>();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer.setCreationListener(r->start());
        renderer.addDrawListener((r,d)->{
            physicsManager.cleanupobjects(this);
            physicsManager.update(d.floatValue());
            update(d);
        });
    }

    public abstract void start();
    public abstract void update(double delta);

    public void addObject(AGameObject object) {
        if (gameObjects.contains(object)) return;
        gameObjects.add(object);
        object.init(this);
    }
    public void addPhysicsObject(AGameObject object) {
        addObject(object);
        physicsManager.addAddObject(object);
    }

    public void destroyObject(AGameObject object) {
        physicsManager.addRemove(object);
    }

    public void deleteObject(AGameObject obj) {
        getRenderer().removeTouchElement(obj);
        obj.destroy(this);
        gameObjects.remove(obj);
    }
}
