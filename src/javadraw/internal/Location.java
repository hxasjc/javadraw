//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Point;
import java.io.Serializable;

public class Location extends ObjectDrawObject implements Serializable {
    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Location(Location point) {
        this(point.x, point.y);
    }

    public Location(Point point) {
        this(point.getX(), point.getY());
    }

    public int getX() {
        return (int)(this.x + (double)0.5F);
    }

    public int getY() {
        return (int)(this.y + (double)0.5F);
    }

    public double getDoubleX() {
        return this.x;
    }

    public double getDoubleY() {
        return this.y;
    }

    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        this.update();
    }

    public Location offset(double dx, double dy) {
        return new Location(this.x + dx, this.y + dy);
    }

    public double distanceTo(Location point) {
        double dx = this.x - point.x;
        double dy = this.y - point.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Point toPoint() {
        return new Point(this.getX(), this.getY());
    }

    /** @deprecated */
    public int hashCode() {
        return this.getX() + (this.getY() << 16);
    }

    public boolean equals(Object other) {
        if (!(other instanceof Location)) {
            return false;
        } else {
            return this.getX() == ((Location)other).getX() && this.getY() == ((Location)other).getY();
        }
    }

    public String toString() {
        return this.toString(Text.formatDecimal(this.x) + ", " + Text.formatDecimal(this.y));
    }
}
