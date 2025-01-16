//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Line2D;

public class Line extends ObjectDrawShape implements Drawable1DInterface, DrawableStrokeInterface {
    private Location end;
    private BasicStroke stroke;

    public Line(double x1, double y1, double x2, double y2, Color color, DrawingCanvas canvas) {
        super(new Location(x1, y1), false, color, canvas);
        this.end = new Location(x2, y2);
        this.end.depend(this);
        this.stroke = DEFAULT_STROKE;
        this.ready();
    }

    public Line(double x1, double y1, double x2, double y2, float thickness, Color color, DrawingCanvas canvas) {
        super(new Location(x1, y1), false, color, canvas);
        this.end = new Location(x2, y2);
        this.end.depend(this);
        this.stroke = new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        this.ready();
    }

    public Line(double x1, double y1, double x2, double y2, float thickness, DrawingCanvas canvas) {
        this(x1, y1, x2, y2, thickness, null, canvas);
    }

    public Line(double x1, double y1, double x2, double y2, DrawingCanvas canvas) {
        this(x1, y1, x2, y2, null, canvas);
    }

    public Line(Location start, Location end, Color color, DrawingCanvas canvas) {
        this(start.getDoubleX(), start.getDoubleY(), end.getDoubleX(), end.getDoubleY(), color, canvas);
    }

    public Line(Location start, Location end, DrawingCanvas canvas) {
        this(start, end, null, canvas);
    }

    /** @deprecated */
    public Shape makeShape() {
        Location start = this.getStart();
        return new Line2D.Double(start.getDoubleX(), start.getDoubleY(), this.end.getDoubleX(), this.end.getDoubleY());
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

    public Location getStart() {
        return this.getMyLocation();
    }

    public Location getEnd() {
        return this.end;
    }

    public void setStart(Location point) {
        this.setStart(point.getDoubleX(), point.getDoubleY());
    }

    public void setStart(double x, double y) {
        Location start = this.getStart();
        start.translate(x - start.getDoubleX(), y - start.getDoubleY());
    }

    public void setEnd(Location point) {
        this.setEnd(point.getDoubleX(), point.getDoubleY());
    }

    public void setEnd(double x, double y) {
        this.end.translate(x - this.end.getDoubleX(), y - this.end.getDoubleY());
    }

    public void setEndPoints(Location start, Location end) {
        this.setStart(start);
        this.setEnd(end);
    }

    public void setEndPoints(double x1, double y1, double x2, double y2) {
        this.setStart(x1, y1);
        this.setEnd(x2, y2);
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
        this.end.translate(dx, dy);
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
        double rx = point.getDoubleX() - this.getStart().getDoubleX();
        double ry = point.getDoubleY() - this.getStart().getDoubleY();
        double r2x = point.getDoubleX() - this.end.getDoubleX();
        double r2y = point.getDoubleY() - this.end.getDoubleY();
        if (Math.max(Math.abs(rx), Math.abs(ry)) < (double)4.0F && Math.sqrt(rx * rx + ry * ry) < (double)4.0F) {
            return true;
        } else if (Math.max(Math.abs(r2x), Math.abs(r2y)) < (double)4.0F && Math.sqrt(r2x * r2x + r2y * r2y) < (double)4.0F) {
            return true;
        } else {
            double length = this.getStart().distanceTo(this.end);
            double dx = this.end.getDoubleX() - this.getStart().getDoubleX();
            double dy = this.end.getDoubleY() - this.getStart().getDoubleY();
            double parameter = (rx * dx + ry * dy) / length;
            double offset = Math.abs((rx * -dy + ry * dx) / length);
            return parameter >= (double)0.0F && parameter <= length && offset <= (double)4.0F;
        }
    }

    public String toString() {
        return this.toString(this.getStart() + ", " + this.getEnd());
    }
}
