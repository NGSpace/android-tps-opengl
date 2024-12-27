package io.github.ngspace.topdownshooter.engine;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.utils.Bounds;
import io.github.ngspace.topdownshooter.utils.Logcat;

public class PhysicsManager {
    private List<AGameObject> colliders = new ArrayList<AGameObject>();
    private List<AGameObject> movementObjects = new ArrayList<AGameObject>();

    public synchronized void addObject(AGameObject object) {
        if (object instanceof MovementPhysics physics) {
            if (physics.caresForOthers()) colliders.add(object);
            movementObjects.add(object);
        } else colliders.add(object);
    }
    public synchronized void removeObject(AGameObject object) {
        colliders.remove(object);
        movementObjects.remove(object);
    }

    /**
     * NO DOCUMENTATION, FUCK YOU FUTURE ME, DEAL WITH THIS SHIT LATER.
     */
    public synchronized void update(float delta) {
        for (var object : movementObjects) {
            var physics = (MovementPhysics) object;
            Bounds oldBounds = object.getBounds();

            float velx = physics.getVelocityX() * delta;
            float vely = physics.getVelocityY() * delta;
            if (velx==0&&vely==0) continue;

            float newVelx = velx;
            float newVely = vely;

            if (velx<0f) newVelx = Math.min(0f,newVelx+physics.getDrag());
            if (velx>0f) newVelx = Math.max(0f,newVelx-physics.getDrag());
            if (vely<0f) newVely = Math.min(0f,newVely+physics.getDrag());
            if (vely>0f) newVely = Math.max(0f,newVely-physics.getDrag());

            physics.setVelocity(newVelx,newVely);

            float newx = oldBounds.x() + velx;
            float newy = oldBounds.y() + vely;

            if (physics.caresForOthers()) {
                for (AGameObject collider : colliders) {
                    if (collider == object) continue;
                    Bounds colliderbounds = collider.getBounds();
                    boolean collided = false;
                    if (oldBounds.positioned(newx, oldBounds.y()).intersects(colliderbounds)) {
                        collided = true;
                        newx = oldBounds.x() > colliderbounds.x() ? colliderbounds.x() + colliderbounds.width() : colliderbounds.x() - oldBounds.width();
                    }
                    if (oldBounds.positioned(oldBounds.x(), newy).intersects(colliderbounds)) {
                        collided = true;
                        newy = oldBounds.y() > colliderbounds.y() ? colliderbounds.y() + colliderbounds.height() : colliderbounds.y() - oldBounds.height();
                    }
                    if (collided) object.collidedWith(collider);
                }
            } else {
                for (AGameObject collider : colliders)
                    if (oldBounds.positioned(newx, newy).intersects(collider.getBounds()))
                        object.collidedWith(collider);
            }
            object.setBounds(oldBounds.positioned(newx,newy));
        }
    }
}
