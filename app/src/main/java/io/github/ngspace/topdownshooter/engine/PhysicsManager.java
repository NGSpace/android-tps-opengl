package io.github.ngspace.topdownshooter.engine;

import java.util.ArrayList;
import java.util.List;

import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.utils.Bounds;

public class PhysicsManager {
    private List<AGameObject> colliders = new ArrayList<AGameObject>();
    private List<AGameObject> movementObjects = new ArrayList<AGameObject>();

    public void addObject(AGameObject object) {
        colliders.add(object);
        if (object instanceof MovementPhysics) movementObjects.add(object);
    }

    /**
     * NO DOCUMENTATION, FUCK YOU FUTURE ME, DEAL WITH THIS SHIT LATER.
     */
    public void update() {
        for (var object : movementObjects) {
            var physics = (MovementPhysics) object;
            Bounds oldBounds = object.getBounds();

            float velx = physics.getVelocityX();
            float vely = physics.getVelocityY();
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

            for (var collider : colliders) {
                if (collider==object) continue;
                var colliderbounds = collider.getBounds();
                if (oldBounds.positioned(newx, oldBounds.y()).intersects(colliderbounds)) newx = oldBounds.x();
                if (oldBounds.positioned(oldBounds.x(),newy).intersects(colliderbounds)) newy = oldBounds.y();
            }
            object.setBounds(oldBounds.positioned(newx,newy));
        }
    }
}
