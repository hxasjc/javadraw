//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;

public class FramedRoundedRect extends ObjectDrawRectangularShape implements DrawableStrokeInterface, Rounded, Serializable {
    private double arcWidth;
    private double arcHeight;
    private BasicStroke stroke;

    public FramedRoundedRect(double x, double y, double width, double height, double arcWidth, double arcHeight, Color color, DrawingCanvas canvas) {
        super(x, y, width, height, false, color, canvas);
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.stroke = DEFAULT_STROKE;
        this.ready();
    }

    public FramedRoundedRect(double x, double y, double width, double height, double arcWidth, double arcHeight, DrawingCanvas canvas) {
        this(x, y, width, height, arcWidth, arcHeight, null, canvas);
    }

    public FramedRoundedRect(Location point, double width, double height, double arcWidth, double arcHeight, Color color, DrawingCanvas canvas) {
        this(point.getDoubleX(), point.getDoubleY(), width, height, arcWidth, arcHeight, color, canvas);
    }

    public FramedRoundedRect(Location point, double width, double height, double arcWidth, double arcHeight, DrawingCanvas canvas) {
        this(point, width, height, arcWidth, arcHeight, null, canvas);
    }

    public FramedRoundedRect(Location corner1, Location corner2, double arcWidth, double arcHeight, Color color, DrawingCanvas canvas) {
        this(corner1, corner2.getDoubleX() - corner1.getDoubleX(), corner2.getDoubleY() - corner1.getDoubleY(), arcWidth, arcHeight, color, canvas);
    }

    public FramedRoundedRect(Location corner1, Location corner2, double arcWidth, double arcHeight, DrawingCanvas canvas) {
        this(corner1, corner2, arcWidth, arcHeight, null, canvas);
    }

    public int getArcHeight() {
        return (int)(this.arcHeight + (double)0.5F);
    }

    public double getDoubleArcHeight() {
        return this.arcHeight;
    }

    public int getArcWidth() {
        return (int)(this.arcWidth + (double)0.5F);
    }

    public double getDoubleArcWidth() {
        return this.arcWidth;
    }

    public void setArcWidth(double a) {
        this.arcWidth = a;
        this.update();
    }

    public void setArcHeight(double a) {
        this.arcHeight = a;
        this.update();
    }

    /** @deprecated */
    public Shape makeShape() {
        return new RoundRectangle2D.Double(this.getDoubleX(), this.getDoubleY(), this.getDoubleWidth(), this.getDoubleHeight(), this.arcWidth, this.arcHeight);
    }

    public double getLineWidth() {
        return this.stroke.getLineWidth();
    }

    public BasicStroke getStroke() {
        return this.stroke;
    }

    public void setLineWidth(double width) {
        this.stroke = new BasicStroke((float)width, this.stroke.getEndCap(), this.stroke.getLineJoin(), this.stroke.getMiterLimit(), this.stroke.getDashArray(), this.stroke.getDashPhase());
        this.update();
    }

    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
        this.update();
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
        return this.toString(", " + Text.formatDecimal(this.arcWidth) + ", " + Text.formatDecimal(this.arcHeight));
    }
}
