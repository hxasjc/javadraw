//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class FilledRect extends ObjectDrawRectangularShape implements Serializable {
    public FilledRect(double x, double y, double width, double height, Color color, DrawingCanvas canvas) {
        super(x, y, width, height, true, color, canvas);
        this.ready();
    }

    public FilledRect(double x, double y, double width, double height, DrawingCanvas canvas) {
        this(x, y, width, height, null, canvas);
    }

    public FilledRect(Location point, double width, double height, Color color, DrawingCanvas canvas) {
        this(point.getDoubleX(), point.getDoubleY(), width, height, color, canvas);
    }

    public FilledRect(Location point, double width, double height, DrawingCanvas canvas) {
        this(point, width, height, null, canvas);
    }

    public FilledRect(Location corner1, Location corner2, Color color, DrawingCanvas canvas) {
        this(corner1, corner2.getDoubleX() - corner1.getDoubleX(), corner2.getDoubleY() - corner1.getDoubleY(), color, canvas);
    }

    public FilledRect(Location corner1, Location corner2, DrawingCanvas canvas) {
        this(corner1, corner2, null, canvas);
    }

    public FilledRect(double x, double y, double width, double height, double angle, Color color, DrawingCanvas canvas) {
        super(x, y, width, height, angle, true, color, canvas);
        this.ready();
    }

    public FilledRect(Location location, double width, double height, double angle, Color color, DrawingCanvas canvas) {
        super(location.getDoubleX(), location.getDoubleY(), width, height, angle, true, color, canvas);
        this.ready();
    }

    public FilledRect(double x, double y, double width, double height, double angle, DrawingCanvas canvas) {
        this(x, y, width, height, angle, null, canvas);
    }

    /** @deprecated */
    public Shape makeShape() {
        return new Rectangle2D.Double(this.getDoubleX(), this.getDoubleY(), this.getDoubleWidth(), this.getDoubleHeight());
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

    public void rotate(double angle) {
        super.rotate(angle);
    }

    public void setRotation(double rotation) {
        super.setRotation(rotation);
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
