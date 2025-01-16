//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

public class FilledOval extends ObjectDrawRectangularShape implements Serializable {
    public FilledOval(double x, double y, double width, double height, Color color, DrawingCanvas canvas) {
        super(x, y, width, height, true, color, canvas);
        this.ready();
    }

    public FilledOval(double x, double y, double width, double height, double rotation, Color color, DrawingCanvas canvas) {
        super(x, y, width, height, rotation, true, color, canvas);
        this.ready();
    }

    public FilledOval(Location location, double width, double height, double rotation, Color color, DrawingCanvas canvas) {
        super(location.getDoubleX(), location.getDoubleY(), width, height, rotation, true, color, canvas);
        this.ready();
    }

    public FilledOval(double x, double y, double width, double height, DrawingCanvas canvas) {
        this(x, y, width, height, null, canvas);
    }

    public FilledOval(Location point, double width, double height, Color color, DrawingCanvas canvas) {
        this(point.getDoubleX(), point.getDoubleY(), width, height, color, canvas);
    }

    public FilledOval(Location point, double width, double height, DrawingCanvas canvas) {
        this(point, width, height, null, canvas);
    }

    public FilledOval(Location corner1, Location corner2, Color color, DrawingCanvas canvas) {
        this(corner1, corner2.getDoubleX() - corner1.getDoubleX(), corner2.getDoubleY() - corner1.getDoubleY(), color, canvas);
    }

    public FilledOval(Location corner1, Location corner2, DrawingCanvas canvas) {
        this(corner1, corner2, null, canvas);
    }

    /** @deprecated */
    public Shape makeShape() {
        return new Ellipse2D.Double(this.getDoubleX(), this.getDoubleY(), this.getDoubleWidth(), this.getDoubleHeight());
    }

    public void setSize(double width, double height) {
        super.setSize(width, height);
    }

    public void setWidth(double w) {
        super.setWidth(w);
    }

    public void setHeight(double h) {
        super.setHeight(h);
    }

    public double getDoubleX() {
        return super.getDoubleX();
    }

    public double getDoubleY() {
        return super.getDoubleY();
    }

    public double getDoubleWidth() {
        return super.getDoubleWidth();
    }

    public double getDoubleHeight() {
        return super.getDoubleHeight();
    }

    public boolean overlaps(Drawable2DInterface other) {
        return super.overlaps(other);
    }

    public void addToCanvas(DrawingCanvas c) {
        super.addToCanvas(c);
    }

    public void moveTo(Location point) {
        super.moveTo(point);
    }

    public void moveTo(double x, double y) {
        super.moveTo(x, y);
    }

    public void move(double dx, double dy) {
        super.move(dx, dy);
    }

    public void setColor(Color c) {
        super.setColor(c);
    }

    public void setColor(Color c, float a) {
        super.setColor(c, a);
    }

    public boolean contains(Location point) {
        return super.contains(point);
    }

}
