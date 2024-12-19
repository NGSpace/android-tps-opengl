package io.github.ngspace.topdownshooter.utils;

import androidx.annotation.NonNull;

public record Bounds(float x, float y, float width, float height) {

    public boolean intersects(Bounds r) {
        float tw = this.width;
        float th = this.height;
        float rw = r.width;
        float rh = r.height;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) return false;
        float tx = this.x;
        float ty = this.y;
        float rx = r.x;
        float ry = r.y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        //      overflow || intersect
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));
    }

    public boolean contains(float otherx, float othery) {
        if (this.width<0||this.height<0) return false;
        if (otherx<x || otherx>x+width) return false;
        if (othery<y || othery>y+height) return false;
        return true;
    }

    @NonNull @Override public String toString() {
        return "Bounds{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public Bounds add(float x, float y) {return new Bounds(this.x+x, this.y+y, width, height);}
    public Bounds positioned(float newx, float newy) {return new Bounds(newx, newy, width, height);}
}
