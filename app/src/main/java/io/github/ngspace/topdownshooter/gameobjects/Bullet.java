package io.github.ngspace.topdownshooter.gameobjects;

import java.util.function.BiFunction;
import java.util.function.Consumer;

import io.github.ngspace.topdownshooter.renderer.renderer.Textures;
import io.github.ngspace.topdownshooter.utils.Logcat;

public class Bullet extends MovementPhysicsSprite {
    private final Entity shooter;
    private boolean destroyed = false;
    private int damage = 0;
    public double lifetime = 0;
    public Consumer<Entity> hitListener;

    public Bullet(Entity shooter, float angle, int damage) {
        super(Textures.CIRCLE, shooter.getX()+shooter.getWidth()/2,shooter.getY()+shooter.getHeight()/2, 10, 10);
        this.shooter = shooter;
        this.damage = damage;
        angle=angle*-1-90f;
        super.setVelocity((float) (250f*Math.cos(Math.toRadians(angle))), (float) (250f*Math.sin(Math.toRadians(angle))));
    }

    @Override
    public void draw(float[] mvpMatrix) {
        Logcat.log(getVelocityX(), getVelocityY());
    }

    @Override public void setVelocity(float x, float y) {}

    @Override public int getDrag() {return 0;}
    @Override public boolean caresForOthers() {return false;}

    @Override public void collidedWith(AGameObject collider) {
        if (destroyed||shooter==collider||collider.isTrigger()) return;
        if (collider instanceof Entity entity) {
            entity.damage(damage);
            if (hitListener!=null)
                hitListener.accept(entity);
            scene.destroyObject(this);
            destroyed=true;
        } else {destroyed=true;scene.destroyObject(this);}
    }
}
