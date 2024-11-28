package io.github.ngspace.topdownshooter.physics;

public interface MovementPhysics {
    public float getVelocityX();
    public float getVelocityY();

    public int getDrag();

    public void setVelocity(float x, float y);
    public void addVelocity(float x, float y);
}
