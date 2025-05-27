package io.github.ngspace.topdownshooter.gameobjects;

import io.github.ngspace.topdownshooter.GeneratedGameScene;
import io.github.ngspace.topdownshooter.renderer.renderer.Textures;

public class Torrent extends Entity {

    double reload = 0;

    public Torrent(float x, float y) {
        super(Textures.TORRENT, x, y, 100, 100);
        health = 1;
    }

    public void aimAndShoot(Entity player, GeneratedGameScene scene, double delta) {
        reload+=delta;
        if (Math.hypot(Math.abs(player.getY() - getY()), Math.abs(player.getX() - getX()))>1000)
            return; // Player too far away

        var angle = getAngleToPlayer(player)-180;
        setAngle(angle);
        if (reload>=1) {
            reload = 0;
            scene.addBullet(new Bullet(this, angle, 10));
        }
    }


    public float getAngleToPlayer(Entity player) {
        return theta(player.getX(),player.getY(), getX(), getY())-360;
    }
    private float theta(float cx, float cy, float x, float y) {
        float angle = (float) (Math.toDegrees(Math.atan2(cy - y, x - cx)) + 90f);
        return (angle <= 180? angle: angle - 360)+180;
    }
}
