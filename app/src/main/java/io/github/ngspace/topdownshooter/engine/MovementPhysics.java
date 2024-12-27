package io.github.ngspace.topdownshooter.engine;

public interface MovementPhysics {
    public float getVelocityX();
    public float getVelocityY();

    public int getDrag();

    public void setVelocity(float x, float y);
    public void addVelocity(float x, float y);

    boolean caresForOthers();
}
