//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;

public class SpawnPacket implements Packet {
    private static final long serialVersionUID = 5031333865827976119L;
    private int id;
    private ObjectType type;
    private boolean filled;
    private String text;
    private int numSides;
    private Location location;
    private double width;
    private double height;
    private double arcWidth;
    private double arcHeight;
    private Color color;
    private double rotation;

    public SpawnPacket() {
        this.color = Color.BLACK;
    }

    public SpawnPacket(ObjectType o) {
        this.color = Color.BLACK;
        this.type = o;
    }

    public SpawnPacket(int id, ObjectType o) {
        this(o);
        this.setID(id);
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public ObjectType getType() {
        return this.type;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int newID) {
        this.id = newID;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumSides() {
        return this.numSides;
    }

    public void setNumSides(int numSides) {
        this.numSides = numSides;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getArcWidth() {
        return this.arcWidth;
    }

    public void setArcWidth(double arcWidth) {
        this.arcWidth = arcWidth;
    }

    public double getArcHeight() {
        return this.arcHeight;
    }

    public void setArcHeight(double arcHeight) {
        this.arcHeight = arcHeight;
    }

    public boolean isFilled() {
        return this.filled;
    }

    public void setFilled(boolean filled) {
        this.filled = filled;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getRotation() {
        return this.rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
