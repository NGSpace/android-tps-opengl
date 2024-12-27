package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.renderer.renderer.Textures;

public class Bullet extends MovementPhysicsSprite {
    private final Entity shooter;
    private boolean destroyed = false;
    private int piercing = 0;
    public double lifetime = 0;

    public Bullet(Entity shooter, float angle, int piercing) {
        super(Textures.CIRCLE, shooter.getX()+shooter.getWidth()/2,shooter.getY()+shooter.getHeight()/2, 10, 10);
        this.shooter = shooter;
        this.piercing = piercing;
        angle=angle*-1-90f;
        super.setVelocity((float) (250f*Math.cos(Math.toRadians(angle))), (float) (250f*Math.sin(Math.toRadians(angle))));
    }

    @Override public void setVelocity(float x, float y) {}

    @Override public int getDrag() {return 0;}
    @Override public boolean caresForOthers() {return false;}

    @Override public void collidedWith(AGameObject collider) {
        if (destroyed||shooter==collider) return;
        if (collider instanceof Entity entity) {
            scene.destroyObject(entity);
            if (--piercing==0) {destroyed=true;scene.destroyObject(this);}
        } else {destroyed=true;scene.destroyObject(this);}
    }
}
