//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import javax.swing.SwingUtilities;

@SuppressWarnings("unused")
public class Client extends NetworkThread {
    private final InetSocketAddress serverAddress;
    private final ClientListener listener;
    private int channelID = -1;
    private String serverName = null;

    Client(String name, InetAddress server) throws IOException {
        super(name);
        this.serverAddress = new InetSocketAddress(server, Network.gamePort);
        this.listener = Network.clientListener;
        this.start();
    }

    public void sendMessage(String message) {
        this.enqueueMessage(message, this.channelID);
    }

    public String getName() {
        return super.getName();
    }

    public String getServerName() {
        return this.serverName;
    }

    public InetSocketAddress getServerAddress() {
        return this.serverAddress;
    }

    public boolean isRunning() {
        return super.isRunning();
    }

    public void shutDown() {
        super.shutDown();
    }

    void messageReceived(int channel, String channelName, final String message) {
        SwingUtilities.invokeLater(() -> Client.this.listener.messageReceived(Client.this, message));
    }

    void channelClosed(int channel, String channelName) {
        if (channel == this.channelID) {
            this.channelID = -1;
            SwingUtilities.invokeLater(() -> Client.this.listener.clientDisconnected(Client.this));
        }

        this.shutDown();
    }

    void channelReady(int channel, String channelName) {
        this.channelID = channel;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Client.this.listener.clientConnected(Client.this);
            }
        });
    }

    boolean setup() {
        if (!super.setup()) {
            return false;
        } else {
            try {
                SocketChannel channel = SocketChannel.open();
                channel.configureBlocking(false);
                channel.connect(this.serverAddress);
                channel.register(this.selector, 8, (Object)null);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                this.closeAll();
                return false;
            }
        }
    }

    void tearDown() {
        Network.closeClient(this);
        super.tearDown();
    }
}
