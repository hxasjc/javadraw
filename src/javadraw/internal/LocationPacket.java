//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public class LocationPacket implements Packet {
    private static final long serialVersionUID = 4601310266730010631L;
    private int id;
    private Location location;

    public LocationPacket() {
    }

    public LocationPacket(Location loc) {
        this.location = loc;
    }

    public void setLocation(Location loc) {
        this.location = loc;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setID(int nID) {
        this.id = nID;
    }

    public int getID() {
        return this.id;
    }
}
