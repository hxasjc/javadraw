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

public class FramedPolygon extends ObjectDrawPolygon implements Polygon, Serializable {
    private int numSides;

    public FramedPolygon(int numSides, double x, double y, double width, double height, double rotation, Color c, DrawingCanvas canvas) {
        super(numSides, x, y, width, height, rotation, false, c, canvas);
        this.numSides = numSides;
        this.ready();
    }

    public FramedPolygon(int numSides, double x, double y, double width, double height, double rotation, DrawingCanvas canvas) {
        super(numSides, x, y, width, height, rotation, false, null, canvas);
        this.numSides = numSides;
        this.ready();
    }

    public FramedPolygon(int numSides, double x, double y, double width, double height, DrawingCanvas canvas) {
        this(numSides, x, y, width, height, 0.0F, canvas);
    }

    public FramedPolygon(int numSides, Location startLoc, double width, double height, DrawingCanvas canvas) {
        this(numSides, startLoc.getX(), startLoc.getY(), width, height, canvas);
    }

    public FramedPolygon(int numSides, Location loc, double width, double height, double rotation, Color color, DrawingCanvas canvas) {
        this(numSides, loc.getDoubleX(), loc.getDoubleY(), width, height, rotation, color, canvas);
    }

    public FramedPolygon(Location[] points, double x, double y, double rotation, Color c, DrawingCanvas canvas) {
        super(points, x, y, rotation, false, c, canvas);
    }

    public Shape makeShape() {
        ArrayList<Location> points = new ArrayList<>();
        double radius = this.getDoubleWidth() / (double)2.0F;
        double angle = 180 - (double) 360 / this.getSides();
        double theta = (Math.PI * 2D) / (double)this.getSides();
        angle = theta;
        Location startLoc = new Location((double)this.getX() + this.getDoubleWidth() / (double)2.0F, (double)this.getY() + this.getDoubleHeight() / (double)2.0F);
        new Location(startLoc);

        for(int i = 0; i < this.getSides(); ++i) {
            Location newLoc = new Location(startLoc);
            newLoc.translate(radius * Math.sin(angle * (double)i), radius * Math.cos(angle * (double)i));
            points.add(newLoc);
        }

        java.awt.Polygon poly = new java.awt.Polygon();

        for(Location l : points) {
            poly.addPoint(l.getX(), l.getY());
        }

        return poly;
    }

    public Shape makeShape2() {
        int diameter = 20;
        int radius = diameter / 2;
        double theta = (Math.PI * 2D) / (double)this.getSides();
        ArrayList<Location> points;
        if (this.regular) {
            points = new ArrayList<>();
        } else {
            points = (ArrayList<Location>) Arrays.asList(this.getPoints());
        }

        if (this.regular) {
            List<Location> locations = new ArrayList<>();

            for(int i = 0; i < this.numSides; ++i) {
                locations.add(new Location((double)radius * Math.sin((Math.PI * 2D) / (double)this.numSides * (double)i), (double)radius * Math.cos((Math.PI * 2D) / (double)this.numSides * (double)i)));
            }

            double scaleFactorX = this.getDoubleWidth() / (double)diameter;
            double scaleFactorY = this.getDoubleHeight() / (double)diameter;
            ArrayList<Location> points2 = new ArrayList<>();

            for(Location point : locations) {
                double newX = scaleFactorX * point.getDoubleX();
                double newY = -scaleFactorY * point.getDoubleY();
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
