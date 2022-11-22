//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public interface ClientListener {
    ClientListener NONE = new ClientListener() {
        public void clientConnected(Client client) {
        }

        public void messageReceived(Client client, String message) {
        }

        public void clientDisconnected(Client client) {
        }
    };

    void clientConnected(Client var1);

    void messageReceived(Client var1, String var2);

    void clientDisconnected(Client var1);
}
