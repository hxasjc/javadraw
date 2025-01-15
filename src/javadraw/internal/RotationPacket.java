//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public class RotationPacket implements Packet {
    private static final long serialVersionUID = -6786047332391806220L;
    private int id;
    private double rotation;

    public RotationPacket() {
    }

    public RotationPacket(double rot) {
        this.rotation = rot;
    }

    public RotationPacket(int id, double rot) {
        this.id = id;
        this.rotation = rot;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public double getRotation() {
        return this.rotation;
    }

    public void setRotation(double rot) {
        this.rotation = rot;
    }
}
