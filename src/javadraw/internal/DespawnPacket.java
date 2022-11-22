//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public class DespawnPacket implements Packet {
    private static final long serialVersionUID = -218655381274892814L;
    private int id;
    private boolean despawn;

    public DespawnPacket() {
        this.despawn = true;
    }

    public DespawnPacket(int id) {
        this.despawn = true;
        this.id = id;
    }

    public DespawnPacket(int id, boolean despawn) {
        this(id);
        this.despawn = despawn;
    }

    public int getID() {
        return this.id;
    }

    public void setID(int newID) {
        this.id = newID;
    }

    public boolean isDespawning() {
        return this.despawn;
    }

    public void setDespawn(boolean val) {
        this.despawn = val;
    }
}
