//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public class VisibilityPacket implements Packet {
    private static final long serialVersionUID = -8060985711953188382L;
    private int id;
    private boolean show;

    public VisibilityPacket() {
    }

    public VisibilityPacket(boolean show) {
        this.show = show;
    }

    public VisibilityPacket(int id, boolean show) {
        this.id = id;
        this.show = show;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public boolean isVisible() {
        return this.show;
    }

    public void setVisible(boolean show) {
        this.show = show;
    }
}
