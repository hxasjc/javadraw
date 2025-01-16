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
import java.util.List;

@SuppressWarnings("unused")
public class FilledPolygon extends ObjectDrawPolygon implements Polygon, Serializable {
    private int numSides;

    public FilledPolygon(int numSides, double x, double y, double width, double height, double rotation, Color c, DrawingCanvas canvas) {
        super(numSides, x, y, width, height, rotation, true, c, canvas);
        this.numSides = numSides;
        this.ready();
    }

    public FilledPolygon(int numSides, double x, double y, double width, double height, double rotation, DrawingCanvas canvas) {
        super(numSides, x, y, width, height, rotation, true, null, canvas);
        this.numSides = numSides;
        this.ready();
    }

    public FilledPolygon(int numSides, double x, double y, double width, double height, DrawingCanvas canvas) {
        this(numSides, x, y, width, height, 0.0F, canvas);
    }

    public FilledPolygon(int numSides, Location loc, double width, double height, double rotation, Color color, DrawingCanvas canvas) {
        this(numSides, loc.getDoubleX(), loc.getDoubleY(), width, height, rotation, color, canvas);
    }

    public FilledPolygon(int numSides, Location loc, double width, double height, double rotation, DrawingCanvas canvas) {
        this(numSides, loc.getDoubleX(), loc.getDoubleY(), width, height, rotation, null, canvas);
    }

    public FilledPolygon(int numSides, double x, double y, double width, double height, Color c, DrawingCanvas canvas) {
        this(numSides, x, y, width, height, 0.0F, c, canvas);
    }

    public FilledPolygon(int numSides, Location loc, double width, double height, DrawingCanvas canvas) {
        this(numSides, loc.getDoubleX(), loc.getDoubleY(), width, height, 0.0F, null, canvas);
    }

    public FilledPolygon(Location[] points, double x, double y, double rotation, Color c, DrawingCanvas canvas) {
        super(points, x, y, rotation, true, c, canvas);
        this.points = points;
        this.ready();
    }

    public FilledPolygon(Location[] points, double x, double y, double rotation, DrawingCanvas canvas) {
        this(points, x, y, rotation, null, canvas);
    }

    public FilledPolygon(Location[] points, double x, double y, Color c, DrawingCanvas canvas) {
        this(points, x, y, 0.0F, c, canvas);
    }

    public FilledPolygon(Location[] points, double x, double y, DrawingCanvas canvas) {
        this(points, x, y, 0.0F, null, canvas);
    }

    public FilledPolygon(Location[] points, Location loc, double rotation, Color c, DrawingCanvas canvas) {
        this(points, loc.getDoubleX(), loc.getDoubleY(), rotation, c, canvas);
    }

    public FilledPolygon(ArrayList<Location> points, double x, double y, double rotation, Color c, DrawingCanvas canvas) {
        super(points.toArray(new Location[0]), x, y, rotation, true, c, canvas);
        this.ready();
    }

    public FilledPolygon(ArrayList<Location> points, Location loc, double rotation, Color c, DrawingCanvas canvas) {
        this(points, loc.getDoubleX(), loc.getDoubleY(), rotation, c, canvas);
    }

    public FilledPolygon(ArrayList<Location> points, Location loc, Color c, DrawingCanvas canvas) {
        this(points, loc.getDoubleX(), loc.getDoubleY(), 0.0F, c, canvas);
    }

    public FilledPolygon(ArrayList<Location> points, double x, double y, double rotation, DrawingCanvas canvas) {
        this(points, x, y, rotation, null, canvas);
    }

    public FilledPolygon(ArrayList<Location> points, double x, double y, DrawingCanvas canvas) {
        this(points, x, y, 0.0F, canvas);
    }

    public Shape makeShape() {
        int DEFAULT_RADIUS = 20;
        List<Location> points = (this.regular ? new ArrayList<>() : (List<Location>) Arrays.asList(this.getPoints()));
        if (this.regular) {
            double radius = (double) DEFAULT_RADIUS / 2;
            double angle = (double)180.0F - (double)360.0F / (double)this.getSides();
            double theta = (Math.PI * 2D) / (double)this.getSides();
            angle = theta;
            Location startLoc = new Location((double)this.getX() + this.getDoubleWidth() / (double)2.0F, (double)this.getY() + this.getDoubleHeight() / (double)2.0F);
            new Location(startLoc);
            double a = (Math.PI * 2D) / (double)this.getSides() * (double)(DEFAULT_RADIUS / 2);
            double n = this.getSides();
            double apothem = a / ((double)2.0F * Math.tan((double)180.0F / n * Math.PI / (double)180.0F));

            for(int i = 0; i < this.getSides(); ++i) {
                Location newLoc = new Location(0.0F, 0.0F);
                newLoc.translate(this.getDoubleWidth() / (double)2.0F * Math.sin(angle * (double)i), (this.getDoubleHeight() / (double)2.0F + apothem / (double)2.0F - (double)1.0F) * -Math.cos(angle * (double)i));
                points.add(newLoc);
            }

            double scaleX = this.getDoubleWidth() / (double)DEFAULT_RADIUS - (double)1.0F;
            double scaleY = this.getDoubleHeight() / (apothem * (double)2.0F) + (double)1.0F;

            for(Location point : points) {
                point.translate(this.getDoubleX() + this.getDoubleWidth() / (double)2.0F, this.getDoubleY() + this.getDoubleHeight() / (double)2.0F + (double)2.5F);
            }
        }

        java.awt.Polygon poly = new java.awt.Polygon();

        for(Location l : points) {
            poly.addPoint(l.getX(), l.getY());
        }

        return poly;
    }

    public Shape makeShape2() {
        System.out.println("test");
        double diameter = this.getDoubleHeight();
        double radius = diameter / (double)2.0F;
        double theta = (Math.PI * 2D) / (double)this.numSides;
        ArrayList<Location> points = this.regular ? new ArrayList<>() : (ArrayList<Location>) Arrays.asList(this.getPoints());
        if (this.regular) {
            List<Location> locations = new ArrayList<>();

            for(int i = 0; i < this.numSides; ++i) {
                locations.add(new Location(radius * Math.sin((Math.PI * 2D) / (double)this.numSides * (double)i) + radius, radius * Math.cos((Math.PI * 2D) / (double)this.numSides * (double)i) + radius));
            }

            System.out.println(locations);
            double scaleFactorX = this.getDoubleWidth() / diameter;
            double scaleFactorY = this.getDoubleHeight() / diameter;
            double cx = 0.0F;
            double cy = 0.0F;
            ArrayList<Location> points2 = new ArrayList<>();

            for(Location point : locations) {
                double newX = scaleFactorX * (point.getDoubleX() - cx) + cx;
                double newY = -scaleFactorY * (point.getDoubleY() - cy) + cy;
                newX += this.getDoubleX() + this.getDoubleWidth() / (double)2.0F;
                newY += this.getDoubleY() + this.getDoubleHeight() / (double)2.0F;
                points2.add(new Location(newX, newY));
            }

            points = points2;
        }

        java.awt.Polygon poly = new java.awt.Polygon();

        for(Location l : points) {
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
