//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public class LayerPacket implements Packet {
    private static final long serialVersionUID = -2392637143938624544L;
    private int id;
    private int layer;

    public LayerPacket() {
    }

    public LayerPacket(int layer) {
        this.layer = layer;
    }

    public LayerPacket(int id, int layer) {
        this.id = id;
        this.layer = layer;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getLayer() {
        return this.layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }
}
