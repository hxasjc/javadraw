//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.io.Serializable;

public class FramedArc extends ObjectDrawRectangularShape implements DrawableStrokeInterface, Serializable {
    private double startAngle;
    private double arcAngle;
    private BasicStroke stroke;

    public FramedArc(double x, double y, double width, double height, double startAngle, double arcAngle, Color color, DrawingCanvas canvas) {
        super(x, y, width, height, false, color, canvas);
        this.startAngle = startAngle;
        this.arcAngle = arcAngle;
        this.stroke = DEFAULT_STROKE;
        this.ready();
    }

    public FramedArc(double x, double y, double width, double height, double startAngle, double arcAngle, DrawingCanvas canvas) {
        this(x, y, width, height, startAngle, arcAngle, (Color)null, canvas);
    }

    public FramedArc(Location point, double width, double height, double startAngle, double arcAngle, Color color, DrawingCanvas canvas) {
        this(point.getDoubleX(), point.getDoubleY(), width, height, startAngle, arcAngle, color, canvas);
    }

    public FramedArc(Location point, double width, double height, double startAngle, double arcAngle, DrawingCanvas canvas) {
        this(point, width, height, startAngle, arcAngle, (Color)null, canvas);
    }

    public FramedArc(Location corner1, Location corner2, double startAngle, double arcAngle, Color color, DrawingCanvas canvas) {
        this(corner1, corner2.getDoubleX() - corner1.getDoubleX(), corner2.getDoubleY() - corner1.getDoubleY(), startAngle, arcAngle, color, canvas);
    }

    public FramedArc(Location corner1, Location corner2, double startAngle, double arcAngle, DrawingCanvas canvas) {
        this(corner1, corner2, startAngle, arcAngle, (Color)null, canvas);
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
        return new Arc2D.Double(this.getDoubleX(), this.getDoubleY(), this.getDoubleWidth(), this.getDoubleHeight(), this.startAngle, this.arcAngle, 0);
    }

    public double getLineWidth() {
        return (double)this.stroke.getLineWidth();
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
        double w = this.getDoubleWidth();
        double h = this.getDoubleHeight();
        double c = Math.PI * ((double)3.0F * (w + h) + Math.sqrt(((double)3.0F * w + h) * (w + (double)3.0F * h))) / (double)2.0F;
        double err = Math.PI / c;
        double cx = this.getDoubleX() + w / (double)2.0F;
        double cy = this.getDoubleY() + h / (double)2.0F;
        double x = point.getDoubleX();
        double y = point.getDoubleY();
        double sa = this.startAngle * Math.PI / (double)180.0F;

        for(double da = this.arcAngle * Math.PI / (double)180.0F; da > err; da /= (double)2.0F) {
            double ma = sa + da / (double)2.0F;
            double dx = cx + Math.cos(-ma) * w / (double)2.0F - x;
            double dy = cy + Math.sin(-ma) * h / (double)2.0F - y;
            double d = Math.sqrt(dx * dx + dy * dy);
            if (d < (double)4.0F) {
                return true;
            }

            if (da * w * Math.sin(-ma) * dx - da * h * Math.cos(-ma) * dy < (double)0.0F) {
                sa = ma;
            }
        }

        return false;
    }

    public String toString() {
        return this.toString(", " + Text.formatDecimal(this.startAngle) + ", " + Text.formatDecimal(this.arcAngle));
    }
}
