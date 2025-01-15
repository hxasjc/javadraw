//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public class SizePacket implements Packet {
    private static final long serialVersionUID = 2350714991382498043L;
    private int id;
    private double width;
    private double height;

    public SizePacket() {
    }

    public SizePacket(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public void setWidth(double newWidth) {
        this.width = newWidth;
    }

    public void setHeight(double newHeight) {
        this.height = newHeight;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int newID) {
        this.id = newID;
    }
}
