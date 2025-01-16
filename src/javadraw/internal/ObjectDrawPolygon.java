//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public abstract class ObjectDrawPolygon extends ObjectDrawShape implements Drawable2DInterface, Resizable2DInterface, Serializable {
    private double width;
    private double height;
    private int sides = 3;
    private double angle;
    protected boolean regular = true;
    protected Location[] points;

    public ObjectDrawPolygon(int numSides, double x, double y, double width, double height, boolean filled, Color c, DrawingCanvas canvas) {
        super(new Location(width < (double)0.0F ? x + width : x, height < (double)0.0F ? y + height : y), filled, c, canvas);
        this.getMyLocation().depend(this);
        this.sides = numSides;
        if (this.sides < 3) {
            this.sides = 3;
        }

        this.width = Math.abs(width);
        this.height = Math.abs(height);
    }

    public ObjectDrawPolygon(int numSides, double x, double y, double width, double height, double rotation, boolean filled, Color c, DrawingCanvas canvas) {
        super(new Location(width < (double)0.0F ? x + width : x, height < (double)0.0F ? y + height : y), rotation, filled, c, canvas);
        this.getMyLocation().depend(this);
        this.sides = numSides;
        if (this.sides < 3) {
            this.sides = 3;
        }

        this.width = Math.abs(width);
        this.height = Math.abs(height);
    }

    public ObjectDrawPolygon(Location[] points, double x, double y, double rotation, boolean filled, Color c, DrawingCanvas canvas) {
        super(new Location(x, y), rotation, filled, c, canvas);
        this.getMyLocation().depend(this);
        this.regular = false;
        this.points = points;
        this.width = 200.0F;
        this.height = 200.0F;
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

    public Location getLocation() {
        return this.getMyLocation();
    }

    public int getSides() {
        return this.sides;
    }

    public Location[] getPoints() {
        return this.points;
    }

    public boolean contains(Location point) {
        return this.getShape().contains(point.getDoubleX(), point.getDoubleY());
    }

    public void setSize(double w, double h) {
        this.width = w;
        this.height = h;
        this.update();
    }

    public void setWidth(double w) {
        this.setSize(w, this.height);
    }

    public void setHeight(double h) {
        this.setSize(this.width, h);
    }

    public int getWidth() {
        return (int)(this.width + (double)0.5F);
    }

    public int getHeight() {
        return (int)(this.height + (double)0.5F);
    }

    public double getDoubleWidth() {
        return this.width;
    }

    public double getDoubleHeight() {
        return this.height;
    }

    public boolean overlaps(Drawable2DInterface other) {
        return this.getBounds().intersects(other.getBounds());
    }

    /** @deprecated */
    public Rectangle2D getBounds() {
        return this.getShape().getBounds2D();
    }

    public Shape makeShape() {
        return null;
    }

    /** @deprecated */
    protected String toString(String middle) {
        return super.toString(Text.formatDecimal(this.getDoubleX()) + ", " + Text.formatDecimal(this.getDoubleY()) + ", " + Text.formatDecimal(this.width) + ", " + Text.formatDecimal(this.height) + middle);
    }
}
