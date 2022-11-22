//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Triangle extends ObjectDrawPolygon implements Serializable {
    public Triangle(double x, double y, double width, double height, boolean filled, Color c, DrawingCanvas canvas) {
        super(3, x, y, width, height, filled, c, canvas);
        this.ready();
    }

    public Triangle(double x, double y, double width, double height, double rotation, boolean filled, Color c, DrawingCanvas canvas) {
        super(3, x, y, width, height, rotation, filled, c, canvas);
        this.ready();
    }

    public Shape makeShape() {
        Polygon poly = new Polygon();
        poly.addPoint(this.getX() + this.getWidth() / 2, this.getY());
        poly.addPoint(this.getX(), this.getY() + this.getHeight());
        poly.addPoint(this.getX() + this.getWidth(), this.getY() + this.getHeight());
        return poly;
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

    public int getX() {
        return super.getX();
    }

    public int getY() {
        return super.getY();
    }

    public double getDoubleX() {
        return super.getDoubleX();
    }

    public double getDoubleY() {
        return super.getDoubleY();
    }

    public Location getLocation() {
        return super.getLocation();
    }

    public int getWidth() {
        return super.getWidth();
    }

    public int getHeight() {
        return super.getHeight();
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

    public Rectangle2D getBounds() {
        return null;
    }

    public void hide() {
        super.hide();
    }

    public void show() {
        super.show();
    }

    public boolean isHidden() {
        return super.isHidden();
    }

    public void addToCanvas(DrawingCanvas c) {
        super.addToCanvas(c);
    }

    public void removeFromCanvas() {
        super.removeFromCanvas();
    }

    public DrawingCanvas getCanvas() {
        return super.getCanvas();
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

    public Color getColor() {
        return super.getColor();
    }

    public void setColor(Color c) {
        super.setColor(c);
    }

    public void setColor(Color c, float a) {
        super.setColor(c, a);
    }

    public void sendForward() {
        super.sendForward();
    }

    public void sendBackward() {
        super.sendBackward();
    }

    public void sendToFront() {
        super.sendToFront();
    }

    public void sendToBack() {
        super.sendToBack();
    }

    public boolean contains(Location point) {
        return super.contains(point);
    }

    public String toString() {
        return super.toString();
    }
}
