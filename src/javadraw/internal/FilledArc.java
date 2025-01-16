//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.io.Serializable;

public class FilledArc extends ObjectDrawRectangularShape implements Serializable {
    private double startAngle;
    private double arcAngle;

    public FilledArc(double x, double y, double width, double height, double startAngle, double arcAngle, Color color, DrawingCanvas canvas) {
        super(x, y, width, height, true, color, canvas);
        this.startAngle = startAngle;
        this.arcAngle = arcAngle;
        this.ready();
    }

    public FilledArc(double x, double y, double width, double height, double startAngle, double arcAngle, DrawingCanvas canvas) {
        this(x, y, width, height, startAngle, arcAngle, null, canvas);
    }

    public FilledArc(Location point, double width, double height, double startAngle, double arcAngle, Color color, DrawingCanvas canvas) {
        this(point.getDoubleX(), point.getDoubleY(), width, height, startAngle, arcAngle, color, canvas);
    }

    public FilledArc(Location point, double width, double height, double startAngle, double arcAngle, DrawingCanvas canvas) {
        this(point, width, height, startAngle, arcAngle, null, canvas);
    }

    public FilledArc(Location corner1, Location corner2, double startAngle, double arcAngle, Color color, DrawingCanvas canvas) {
        this(corner1, corner2.getDoubleX() - corner1.getDoubleX(), corner2.getDoubleY() - corner1.getDoubleY(), startAngle, arcAngle, color, canvas);
    }

    public FilledArc(Location corner1, Location corner2, double startAngle, double arcAngle, DrawingCanvas canvas) {
        this(corner1, corner2, startAngle, arcAngle, null, canvas);
    }

    public double getArcAngle() {
        return this.arcAngle;
    }

    public double getStartAngle() {
        return this.startAngle;
    }

    public void setStartAngle(double a) {
        this.startAngle = a;
        this.update();
    }

    public void setArcAngle(double a) {
        this.arcAngle = a;
        this.update();
    }

    /** @deprecated */
    public Shape makeShape() {
        return new Arc2D.Double(this.getDoubleX(), this.getDoubleY(), this.getDoubleWidth(), this.getDoubleHeight(), this.startAngle, this.arcAngle, Arc2D.PIE);
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

    public String toString() {
        return this.toString(", " + Text.formatDecimal(this.startAngle) + ", " + Text.formatDecimal(this.arcAngle));
    }
}
