package io.github.ngspace.topdownshooter.physics;

import java.util.ArrayList;
import java.util.List;

import io.github.ngspace.topdownshooter.MainActivity;
import io.github.ngspace.topdownshooter.gameobjects.AGameObject;
import io.github.ngspace.topdownshooter.renderer.opengl.Bounds;

public class PhysicsManager {
    private List<AGameObject> colliders = new ArrayList<AGameObject>();
    private List<AGameObject> movementObjects = new ArrayList<AGameObject>();

    public void addObject(AGameObject object) {
        colliders.add(object);
        if (object instanceof MovementPhysics) movementObjects.add(object);
    }
    public void update() {
        for (var object : movementObjects) {
            var physics = (MovementPhysics) object;

            float velx = physics.getVelocityX();
            float vely = physics.getVelocityY();
            Bounds newBounds = object.getBounds().add(velx, vely);

            float newVelx = velx;
            float newVely = vely;

            if (velx<0) newVelx = Math.min(0,newVelx+physics.getDrag());
            if (velx>0) newVelx = Math.max(0,newVelx-physics.getDrag());
            if (vely<0) newVely = Math.min(0,newVely+physics.getDrag());
            if (vely>0) newVely = Math.max(0,newVely-physics.getDrag());
            physics.setVelocity(newVelx,newVely);

            for (var collider : colliders) {
                if (collider==object) continue;
                var colliderbounds = collider.getBounds();
                if (newBounds.intersects(colliderbounds)) {
                    if (newBounds.x()<colliderbounds.x()+colliderbounds.width()&&newBounds.x()>colliderbounds.x()
                            &&(newBounds.y()<colliderbounds.y()+colliderbounds.height()&&newBounds.y()>colliderbounds.y())) velx = 0;
                    newBounds = object.getBounds().add(velx, vely);
                    if (newBounds.y()<colliderbounds.y()+colliderbounds.height()&&newBounds.y()>colliderbounds.y()
                            &&(newBounds.x()<colliderbounds.x()+colliderbounds.width()&&newBounds.x()>colliderbounds.x())) vely = 0;
                    newBounds = object.getBounds().add(velx, vely);
                }
            }
            object.setBounds(newBounds);
        }
    }
}
