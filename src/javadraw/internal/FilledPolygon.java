//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Shape;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class FilledPolygon extends ObjectDrawPolygon implements Polygon, Serializable {
    private int numSides;

    public FilledPolygon(int numSides, double x, double y, double width, double height, double rotation, Color c, DrawingCanvas canvas) {
        super(numSides, x, y, width, height, rotation, true, c, canvas);
        this.numSides = numSides;
        this.ready();
    }

    public FilledPolygon(int numSides, double x, double y, double width, double height, double rotation, DrawingCanvas canvas) {
        super(numSides, x, y, width, height, rotation, true, (Color)null, canvas);
        this.numSides = numSides;
        this.ready();
    }

    public FilledPolygon(int numSides, double x, double y, double width, double height, DrawingCanvas canvas) {
        this(numSides, x, y, width, height, 0.0, canvas);
    }

    public FilledPolygon(int numSides, Location loc, double width, double height, double rotation, Color color, DrawingCanvas canvas) {
        this(numSides, loc.getDoubleX(), loc.getDoubleY(), width, height, rotation, color, canvas);
    }

    public FilledPolygon(int numSides, Location loc, double width, double height, double rotation, DrawingCanvas canvas) {
        this(numSides, loc.getDoubleX(), loc.getDoubleY(), width, height, rotation, (Color)null, canvas);
    }

    public FilledPolygon(int numSides, double x, double y, double width, double height, Color c, DrawingCanvas canvas) {
        this(numSides, x, y, width, height, 0.0, c, canvas);
    }

    public FilledPolygon(int numSides, Location loc, double width, double height, DrawingCanvas canvas) {
        this(numSides, loc.getDoubleX(), loc.getDoubleY(), width, height, 0.0, (Color)null, canvas);
    }

    public FilledPolygon(Location[] points, double x, double y, double rotation, Color c, DrawingCanvas canvas) {
        super(points, x, y, rotation, true, c, canvas);
        this.points = points;
        this.ready();
    }

    public FilledPolygon(Location[] points, double x, double y, double rotation, DrawingCanvas canvas) {
        this((Location[])points, x, y, rotation, (Color)null, canvas);
    }

    public FilledPolygon(Location[] points, double x, double y, Color c, DrawingCanvas canvas) {
        this(points, x, y, 0.0, c, canvas);
    }

    public FilledPolygon(Location[] points, double x, double y, DrawingCanvas canvas) {
        this((Location[])points, x, y, 0.0, (Color)null, canvas);
    }

    public FilledPolygon(Location[] points, Location loc, double rotation, Color c, DrawingCanvas canvas) {
        this(points, loc.getDoubleX(), loc.getDoubleY(), rotation, c, canvas);
    }

    public FilledPolygon(ArrayList<Location> points, double x, double y, double rotation, Color c, DrawingCanvas canvas) {
        super((Location[])points.toArray(new Location[points.size()]), x, y, rotation, true, c, canvas);
        this.ready();
    }

    public FilledPolygon(ArrayList<Location> points, Location loc, double rotation, Color c, DrawingCanvas canvas) {
        this(points, loc.getDoubleX(), loc.getDoubleY(), rotation, c, canvas);
    }

    public FilledPolygon(ArrayList<Location> points, Location loc, Color c, DrawingCanvas canvas) {
        this(points, loc.getDoubleX(), loc.getDoubleY(), 0.0, c, canvas);
    }

    public FilledPolygon(ArrayList<Location> points, double x, double y, double rotation, DrawingCanvas canvas) {
        this((ArrayList)points, x, y, rotation, (Color)null, canvas);
    }

    public FilledPolygon(ArrayList<Location> points, double x, double y, DrawingCanvas canvas) {
        this(points, x, y, 0.0, canvas);
    }

    public Shape makeShape() {
        int DEFAULT_RADIUS = 20;
        List<Location> points = this.regular ? new ArrayList() : Arrays.asList(this.getPoints());
        if (this.regular) {
            double radius = (double)(DEFAULT_RADIUS / 2);
            double angle = 180.0 - 360.0 / (double)this.getSides();
            double theta = 6.283185307179586 / (double)this.getSides();
            angle = theta;
            Location startLoc = new Location((double)this.getX() + this.getDoubleWidth() / 2.0, (double)this.getY() + this.getDoubleHeight() / 2.0);
            new Location(startLoc);
            double a = 6.283185307179586 / (double)this.getSides() * (double)(DEFAULT_RADIUS / 2);
            double n = (double)this.getSides();
            double apothem = a / (2.0 * Math.tan(180.0 / n * Math.PI / 180.0));

            for(int i = 0; i < this.getSides(); ++i) {
                Location newLoc = new Location(0.0, 0.0);
                newLoc.translate(this.getDoubleWidth() / 2.0 * Math.sin(angle * (double)i), (this.getDoubleHeight() / 2.0 + apothem / 2.0 - 1.0) * -Math.cos(angle * (double)i));
                ((List)points).add(newLoc);
            }

            double scaleX = this.getDoubleWidth() / (double)DEFAULT_RADIUS - 1.0;
            double scaleY = this.getDoubleHeight() / (apothem * 2.0) + 1.0;
            Iterator var21 = ((List)points).iterator();

            while(var21.hasNext()) {
                Location point = (Location)var21.next();
                point.translate(this.getDoubleX() + this.getDoubleWidth() / 2.0, this.getDoubleY() + this.getDoubleHeight() / 2.0 + 2.5);
            }
        }

        java.awt.Polygon poly = new java.awt.Polygon();
        Iterator var4 = ((List)points).iterator();

        while(var4.hasNext()) {
            Location l = (Location)var4.next();
            poly.addPoint(l.getX(), l.getY());
        }

        return poly;
    }

    public Shape makeShape2() {
        System.out.println("test");
        double diameter = this.getDoubleHeight();
        double radius = diameter / 2.0;
        double theta = 6.283185307179586 / (double)this.numSides;
        List<Location> points = this.regular ? new ArrayList() : (ArrayList)Arrays.asList(this.getPoints());
        if (this.regular) {
            List<Location> locations = new ArrayList();

            for(int i = 0; i < this.numSides; ++i) {
                locations.add(new Location(radius * Math.sin(6.283185307179586 / (double)this.numSides * (double)i) + radius, radius * Math.cos(6.283185307179586 / (double)this.numSides * (double)i) + radius));
            }

            System.out.println(locations);
            double scaleFactorX = this.getDoubleWidth() / diameter;
            double scaleFactorY = this.getDoubleHeight() / diameter;
            double cx = 0.0;
            double cy = 0.0;
            List<Location> points2 = new ArrayList();
            Iterator var18 = locations.iterator();

            while(var18.hasNext()) {
                Location point = (Location)var18.next();
                double newX = scaleFactorX * (point.getDoubleX() - cx) + cx;
                double newY = -scaleFactorY * (point.getDoubleY() - cy) + cy;
                newX += this.getDoubleX() + this.getDoubleWidth() / 2.0;
                newY += this.getDoubleY() + this.getDoubleHeight() / 2.0;
                points2.add(new Location(newX, newY));
            }

            points = points2;
        }

        java.awt.Polygon poly = new java.awt.Polygon();
        Iterator var26 = points.iterator();

        while(var26.hasNext()) {
            Location l = (Location)var26.next();
            poly.addPoint(l.getX(), l.getY());
        }

        return poly;
    }

    public int getNumSides() {
        return this.numSides;
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
        return super.toString();
    }
}
