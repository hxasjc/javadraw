//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;

public class ColorPacket implements Packet {
    private static final long serialVersionUID = 1998157566392847725L;
    private int id;
    private Color color;

    public ColorPacket() {
    }

    public ColorPacket(Color color) {
        this.color = color;
    }

    public ColorPacket(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }

    public ColorPacket(int id, Color color) {
        this.id = id;
        this.color = color;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color newColor) {
        this.color = newColor;
    }
}
