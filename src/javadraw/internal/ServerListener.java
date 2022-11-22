//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public interface ServerListener {
    ServerListener ECHO = new ServerListener() {
        public void channelClosed(Server server, int channelID, String channelName) {
        }

        public void channelOpened(Server server, int channelID, String channelName) {
        }

        public void serverStarted(Server server) {
        }

        public void serverStopped(Server server) {
        }

        public void messageReceived(Server server, int channelID, String channelName, String message) {
            server.sendMessage(message);
        }
    };

    void serverStarted(Server var1);

    void channelOpened(Server var1, int var2, String var3);

    void messageReceived(Server var1, int var2, String var3, String var4);

    void channelClosed(Server var1, int var2, String var3);

    void serverStopped(Server var1);
}
