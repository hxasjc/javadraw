//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

/** @deprecated */
public abstract class ObjectDrawRectangularShape extends ObjectDrawShape implements Drawable2DInterface, Resizable2DInterface {
    private double width;
    private double height;

    public ObjectDrawRectangularShape(double x, double y, double width, double height, boolean filled, Color c, DrawingCanvas canvas) {
        super(new Location(width < (double)0.0F ? x + width : x, height < (double)0.0F ? y + height : y), filled, c, canvas);
        this.getMyLocation().depend(this);
        this.width = Math.abs(width);
        this.height = Math.abs(height);
    }

    public ObjectDrawRectangularShape(double x, double y, double width, double height, double rotation, boolean filled, Color c, DrawingCanvas canvas) {
        super(new Location(width < (double)0.0F ? x + width : x, height < (double)0.0F ? y + height : y), rotation, filled, c, canvas);
        this.getMyLocation().depend(this);
        this.width = Math.abs(width);
        this.height = Math.abs(height);
    }

    public boolean contains(Location point) {
        return this.getShape().contains(point.getDoubleX(), point.getDoubleY());
    }

    public Location getLocation() {
        return this.getMyLocation();
    }

    public int getHeight() {
        return (int)(this.height + (double)0.5F);
    }

    public double getDoubleHeight() {
        return this.height;
    }

    public int getWidth() {
        return (int)(this.width + (double)0.5F);
    }

    public double getDoubleWidth() {
        return this.width;
    }

    public int getX() {
        return this.getLocation().getX();
    }

    public int getY() {
        return this.getLocation().getY();
    }

    public double getDoubleX() {
        return this.getLocation().getDoubleX();
    }

    public double getDoubleY() {
        return this.getLocation().getDoubleY();
    }

    /** @deprecated */
    public Rectangle2D getBounds() {
        return this.getShape().getBounds2D();
    }

    public boolean overlaps(Drawable2DInterface other) {
        return this.getBounds().intersects(other.getBounds());
    }

    public void setHeight(double h) {
        this.setSize(this.width, h);
    }

    public void setSize(double w, double h) {
        this.width = w;
        this.height = h;
        this.update();
    }

    public void setWidth(double w) {
        this.setSize(w, this.height);
    }

    /** @deprecated */
    protected String toString(String middle) {
        return super.toString(Text.formatDecimal(this.getDoubleX()) + ", " + Text.formatDecimal(this.getDoubleY()) + ", " + Text.formatDecimal(this.width) + ", " + Text.formatDecimal(this.height) + middle);
    }
}
