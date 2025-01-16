//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.util.WeakHashMap;

public class CoffeeTester extends WindowController implements ClientListener {
    private static final long serialVersionUID = 7579274715942148859L;
    private Client client;
    private final WeakHashMap<Integer, DrawableInterface> clients = new WeakHashMap<>();

    public CoffeeTester() {
    }

    public static void main(String[] args) {
        (new CoffeeTester()).startController(800, 600);
    }

    public void begin() {
        Network.configure("CoffeeTester", new ServerListener() {
            public void serverStarted(Server server) {
                new FilledRect(0.0F, 0.0F, 10.0F, 10.0F, Color.GREEN, CoffeeTester.this.canvas);
            }

            public void channelOpened(Server server, int channelID, String channelName) {
                server.sendMessage(Packet.serialize(new LocationPacket(new Location(150.0F, 150.0F))));
            }

            public void messageReceived(Server server, int channelID, String channelName, String message) {
            }

            public void channelClosed(Server server, int channelID, String channelName) {
            }

            public void serverStopped(Server server) {
            }
        }, this);
        Network.startServer("CoffeeTester");
    }

    public void clientConnected(Client client) {
        this.client = client;
    }

    public void messageReceived(Client client, String message) {
        Packet packet = Packet.deserialize(message);
        if (packet instanceof LocationPacket) {
            LocationPacket locPacket = (LocationPacket)packet;
            this.clients.get(locPacket.getID()).moveTo(locPacket.getLocation());
        }

    }

    public void clientDisconnected(Client client) {
        this.client = null;
    }

    public void onSpacePress() {
        Packet packet = new LocationPacket(new Location(Math.random() * (double)10.0F, Math.random() * (double)10.0F));
        String message = Packet.serialize(packet);
        this.client.sendMessage(message);
    }
}
