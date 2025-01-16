//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.charset.StandardCharsets;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Server extends NetworkThread {
    private final ServerSocketChannel serverSocket;
    private final ServerListener listener;
    private final MulticastAnnouncerChannel announcer;
    private final Runnable onLoad;

    Server(String name, Runnable onLoad) throws IOException {
        super(name);
        this.onLoad = onLoad;
        this.serverSocket = ServerSocketChannel.open();
        this.announcer = new MulticastAnnouncerChannel();
        this.listener = Network.serverListener;
        this.start();
    }

    public void sendMessage(String message, int channelID) {
        this.enqueueMessage(message, channelID);
    }

    public void sendMessage(String message) {
        this.enqueueMessage(message, -1);
    }

    public String getName() {
        return super.getName();
    }

    void messageReceived(final int channel, final String channelName, final String message) {
        SwingUtilities.invokeLater(() -> Server.this.listener.messageReceived(Server.this, channel, channelName, message));
    }

    void channelClosed(final int channel, final String channelName) {
        SwingUtilities.invokeLater(() -> Server.this.listener.channelClosed(Server.this, channel, channelName));
    }

    void channelReady(final int channel, final String channelName) {
        SwingUtilities.invokeLater(() -> Server.this.listener.channelOpened(Server.this, channel, channelName));
    }

    boolean setup() {
        if (!super.setup()) {
            return false;
        } else {
            try {
                this.serverSocket.configureBlocking(false);
                InetSocketAddress isa = new InetSocketAddress(Network.gamePort);
                this.serverSocket.socket().bind(isa, 100);
                this.serverSocket.register(this.selector, SelectionKey.OP_ACCEPT, null);
            } catch (IOException e) {
                e.printStackTrace();
                this.closeAll();
                return false;
            }

            try {
                this.announcer.setup();
            } catch (IOException e) {
                e.printStackTrace();
            }

            SwingUtilities.invokeLater(() -> {
                Server.this.listener.serverStarted(Server.this);
                if (Server.this.onLoad != null) {
                    Server.this.onLoad.run();
                }

            });
            return true;
        }
    }

    void tearDown() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> Server.this.listener.serverStopped(Server.this));
        Network.closeServer(this);
        super.tearDown();
    }

    private class MulticastAnnouncerChannel implements NetworkThread.ChannelHandler, ActionListener {
        private final DatagramChannel channel = DatagramChannel.open();
        private final Timer timer = new Timer(100, this);
        private final ByteBuffer message;

        public MulticastAnnouncerChannel() throws IOException {
            this.message = ByteBuffer.wrap((Server.this.getName() + "\n" + Network.gameIdentifier).getBytes(StandardCharsets.ISO_8859_1));
        }

        public void setup() throws IOException {
            this.channel.configureBlocking(false);
            this.channel.connect(new InetSocketAddress(Network.lobbyAddress, Network.lobbyPort));
            this.channel.register(Server.this.selector, 0, this);
            this.timer.start();
        }

        public void close() {
            this.timer.stop();

            try {
                this.channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void read() {
        }

        public void write() {
            try {
                this.channel.write(this.message);
            } catch (IOException e) {
                e.printStackTrace();
                this.close();
            }

            if (this.message.remaining() == 0) {
                this.message.rewind();
                Server.this.updateChannelLater(this.channel, 0, 4);
            }

        }

        public void actionPerformed(ActionEvent e) {
            Server.this.updateChannelLater(this.channel, 4, 4);
            Server.this.selector.wakeup();
        }
    }
}
