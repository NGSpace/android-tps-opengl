package io.github.ngspace.topdownshooter.engine;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.renderer.OpenGLActivity;

public abstract class GameScene extends OpenGLActivity {

    private PhysicsManager physicsManager = new PhysicsManager();
    private List<AGameObject> gameObjects = new ArrayList<AGameObject>();
    private Stack<AGameObject> objectsToDestroy = new Stack<AGameObject>();

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        renderer.setCreationListener(r->start());
        renderer.addDrawListener((r,d)->{physicsManager.update(d.floatValue());cleanupobjects();update(d);});
    }

    private void cleanupobjects() {
        while (!objectsToDestroy.empty()) {
            AGameObject obj = objectsToDestroy.pop();
            gameObjects.remove(obj);
            physicsManager.removeObject(obj);
            obj.destroy(this);
        }
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
        physicsManager.addObject(object);
    }

    public void destroyObject(AGameObject object) {
//        gameObjects.remove(object);
//        physicsManager.removeObject(object);
//        object.destroy(this);
        objectsToDestroy.add(object);
    }
}
