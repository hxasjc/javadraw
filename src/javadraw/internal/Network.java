//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Network {
    static String gameIdentifier = null;
    static ServerListener serverListener = null;
    static ClientListener clientListener = null;
    static String lobbyAddress = "239.255.15.4";
    static int lobbyPort = 21504;
    static int gamePort = 21504;
    private static JDialog hostDialog = null;
    private static JDialog joinDialog = null;
    private static final ArrayList framesNeedingMenu = new ArrayList();
    private static final Action hostAction = new AbstractAction("Host network game") {
        public void actionPerformed(ActionEvent e) {
            Network.openHostDialog();
        }
    };
    private static final Action joinAction = new AbstractAction("Join network game") {
        public void actionPerformed(ActionEvent e) {
            Network.openJoinDialog();
        }
    };

    public Network() {
    }

    static void frameCreated(ControllerFrame frame) {
        if (isConfigured()) {
            addMenuToFrame(frame);
        } else {
            framesNeedingMenu.add(frame);
        }

    }

    private static void addMenuToFrame(ControllerFrame frame) {
        JMenu menu = new JMenu("Network");
        menu.add(new JMenuItem(joinAction));
        menu.add(new JMenuItem(hostAction));
        JMenuBar menuBar = frame.getControllerMenubar();
        menuBar.add(menu);
        menuBar.revalidate();
    }

    public static void configure(String game, ServerListener server, ClientListener client) {
        if (game == null) {
            throw new IllegalArgumentException("The game identifier cannot be null.");
        } else if (server == null) {
            throw new IllegalArgumentException("The server listener cannot be null.");
        } else if (client == null) {
            throw new IllegalArgumentException("The client listener cannot be null.");
        } else {
            gameIdentifier = game;
            serverListener = server;
            clientListener = client;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    for(int i = 0; i < Network.framesNeedingMenu.size(); ++i) {
                        Network.addMenuToFrame((ControllerFrame)Network.framesNeedingMenu.get(i));
                    }

                    Network.framesNeedingMenu.clear();
                }
            });
        }
    }

    private static boolean isConfigured() {
        return gameIdentifier != null;
    }

    static void closeServer(Server server) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Network.hostAction.setEnabled(true);
            }
        });
    }

    static void closeClient(Client client) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Network.joinAction.setEnabled(true);
            }
        });
    }

    public static void startServer(String name) {
        if (!isConfigured()) {
            throw new IllegalStateException("You must call configure() first.");
        } else if (!hostAction.isEnabled()) {
            throw new IllegalStateException("You may only have one server running at a time.");
        } else {
            startServer(name, (Runnable)null);
        }
    }

    private static void startServer(String name, Runnable onLoad) {
        try {
            new Server(name, onLoad);
            hostAction.setEnabled(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void connectToServer(InetAddress address, String name) {
        if (!joinAction.isEnabled()) {
            throw new IllegalStateException("You may only have one server running at a time.");
        } else {
            try {
                new Client(name, address);
                joinAction.setEnabled(false);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void connectToServer(String address, String name) {
        if (!isConfigured()) {
            throw new IllegalStateException("You must call configure() first.");
        } else {
            try {
                connectToServer(InetAddress.getByName(address), name);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }

        }
    }

    private static JDialog createHostDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle((String)hostAction.getValue("Name"));
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(1);
        JPanel content = new JPanel();
        dialog.setContentPane(content);
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.setLayout(new BoxLayout(content, 1));
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, 0));
        namePanel.add(new JLabel("Game name: "));
        JTextField gameName = new JTextField();
        namePanel.add(gameName);
        content.add(namePanel);
        content.add(Box.createVerticalStrut(10));
        JPanel joinPanel = new JPanel();
        joinPanel.setLayout(new BoxLayout(joinPanel, 0));
        JCheckBox joinBox = new JCheckBox("Also join as: ");
        joinBox.setSelected(true);
        joinPanel.add(joinBox);
        JTextField joinName = new JTextField();
        joinPanel.add(joinName);
        content.add(joinPanel);
        content.add(Box.createVerticalStrut(20));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        buttonPanel.add(Box.createHorizontalStrut(150));
        JButton cancel = new JButton("Cancel");
        buttonPanel.add(cancel);
        JButton host = new JButton("Host");
        dialog.getRootPane().setDefaultButton(host);
        buttonPanel.add(host);
        content.add(buttonPanel);
        new HostListener(dialog, cancel, host, joinBox, gameName, joinName);
        dialog.pack();
        return dialog;
    }

    public static void openHostDialog() {
        if (hostDialog == null) {
            hostDialog = createHostDialog();
        }

        hostDialog.show();
    }

    private static JDialog createJoinDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle((String)hostAction.getValue("Name"));
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(1);
        JPanel content = new JPanel();
        dialog.setContentPane(content);
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.setLayout(new BoxLayout(content, 1));
        JList hostList = new JList();
        content.add(new JScrollPane(hostList));
        content.add(Box.createVerticalStrut(10));
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, 0));
        namePanel.add(new JLabel("Join as: "));
        JTextField joinName = new JTextField();
        namePanel.add(joinName);
        content.add(namePanel);
        content.add(Box.createVerticalStrut(20));
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, 0));
        buttonPanel.add(Box.createHorizontalStrut(150));
        JButton cancel = new JButton("Cancel");
        buttonPanel.add(cancel);
        JButton join = new JButton("Join");
        dialog.getRootPane().setDefaultButton(join);
        buttonPanel.add(join);
        content.add(buttonPanel);

        try {
            new JoinListener(dialog, hostList, joinName, cancel, join);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.pack();
        return dialog;
    }

    public static void openJoinDialog() {
        if (joinDialog == null) {
            joinDialog = createJoinDialog();
        }

        joinDialog.show();
    }

    public static void setLobbyAddress(String address) {
        lobbyAddress = address;
    }

    public static void setLobbyPort(int port) {
        lobbyPort = port;
    }

    public static void setGamePort(int port) {
        gamePort = port;
    }

    private static class HostListener extends WindowAdapter implements CaretListener, ActionListener, ItemListener {
        private final JDialog dialog;
        private final JButton cancel;
        private final JButton host;
        private final JCheckBox joinBox;
        private final JTextField gameName;
        private final JTextField joinName;

        public HostListener(JDialog dialog, JButton cancel, JButton host, JCheckBox joinBox, JTextField gameName, JTextField joinName) {
            this.dialog = dialog;
            this.cancel = cancel;
            this.host = host;
            this.joinBox = joinBox;
            this.gameName = gameName;
            this.joinName = joinName;
            dialog.addWindowListener(this);
            cancel.addActionListener(this);
            host.addActionListener(this);
            joinBox.addItemListener(this);
            gameName.addCaretListener(this);
            joinName.addCaretListener(this);
            this.checkCanJoin();
        }

        private void checkCanJoin() {
            this.host.setEnabled(this.gameName.getText().length() > 0 && (!this.joinBox.isSelected() || this.joinName.getText().length() > 0));
        }

        public void caretUpdate(CaretEvent e) {
            this.checkCanJoin();
        }

        public void windowActivated(WindowEvent e) {
            this.gameName.requestFocusInWindow();
        }

        public void windowDeactivated(WindowEvent e) {
            this.gameName.setText("");
            this.joinName.setText("");
            this.joinBox.setSelected(true);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == this.host) {
                final String joinAs = this.joinName.getText();
                Runnable onLoad = !this.joinBox.isSelected() ? null : new Runnable() {
                    public void run() {
                        Network.connectToServer("127.0.0.1", joinAs);
                    }
                };
                Network.startServer(this.gameName.getText(), onLoad);
            }

            this.dialog.hide();
        }

        public void itemStateChanged(ItemEvent e) {
            if (e.getStateChange() == 1) {
                this.joinName.setEnabled(true);
            } else {
                this.joinName.setText("");
                this.joinName.setEnabled(false);
            }

            this.checkCanJoin();
        }
    }

    private static class DiscoveryThread extends AbstractListModel implements Runnable {
        private static final int DATA_LENGTH = 256;
        private static final int SERVER_TIMEOUT = 1000;
        private final DatagramPacket dgram = new DatagramPacket(new byte[256], 256);
        private final MulticastSocket socket;
        private final ArrayList discoveries = new ArrayList();
        private boolean cancelled = false;

        public DiscoveryThread() throws IOException {
            this.socket = new MulticastSocket(Network.lobbyPort);
            this.socket.joinGroup(InetAddress.getByName(Network.lobbyAddress));
        }

        public void run() {
            try {
                for(; !this.cancelled; this.dgram.setLength(256)) {
                    this.socket.receive(this.dgram);
                    String message = new String(this.dgram.getData(), this.dgram.getOffset(), this.dgram.getLength(), "ISO-8859-1");
                    String serverName = message.substring(0, message.indexOf(10));
                    String gameName = message.substring(message.lastIndexOf(10) + 1);
                    if (gameName.equals(Network.gameIdentifier)) {
                        new Discovery(serverName, this.dgram.getAddress());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.cancelled = false;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    DiscoveryThread.this.clear();
                }
            });
        }

        public void checkDiscovery(Discovery discovery) {
            int insertIndex = -2;

            for(int i = 0; i < this.discoveries.size(); ++i) {
                Discovery old = (Discovery)this.discoveries.get(i);
                if (old.serverAddress.equals(discovery.serverAddress)) {
                    old.timeStamp = discovery.timeStamp;
                    insertIndex = -1;
                } else if (old.timeStamp + 1000L < discovery.timeStamp) {
                    this.discoveries.remove(i);
                    this.fireIntervalRemoved(this, i, i);
                    --i;
                } else if (old.serverName.compareTo(discovery.serverName) > 0) {
                    insertIndex = i;
                }
            }

            if (insertIndex == -2) {
                insertIndex = this.discoveries.size();
            }

            if (insertIndex >= 0) {
                this.discoveries.add(insertIndex, discovery);
                this.fireIntervalAdded(this, insertIndex, insertIndex);
            }

        }

        public void cancel() {
            this.cancelled = true;
        }

        public void clear() {
            int length = this.discoveries.size();
            this.discoveries.clear();
            this.fireIntervalRemoved(this, 0, length - 1);
        }

        public Object getElementAt(int index) {
            return this.discoveries.get(index);
        }

        public int getSize() {
            return this.discoveries.size();
        }

        public class Discovery implements Runnable {
            private final String serverName;
            private final InetAddress serverAddress;
            private long timeStamp;

            public Discovery(String serverName, InetAddress serverAddress) {
                this.serverName = serverName;
                this.serverAddress = serverAddress;
                this.timeStamp = System.currentTimeMillis();
                SwingUtilities.invokeLater(this);
            }

            public void run() {
                DiscoveryThread.this.checkDiscovery(this);
            }

            public String toString() {
                return this.serverName;
            }
        }
    }

    private static class JoinListener extends WindowAdapter implements ListSelectionListener, CaretListener, ActionListener, ListDataListener {
        private final DiscoveryThread discoverer = new DiscoveryThread();
        private final JDialog dialog;
        private final JButton join;
        private final JButton cancel;
        private final JList list;
        private final JTextField joinName;

        public JoinListener(JDialog dialog, JList list, JTextField joinName, JButton cancel, JButton join) throws IOException {
            this.dialog = dialog;
            this.join = join;
            this.cancel = cancel;
            this.list = list;
            this.joinName = joinName;
            list.setModel(this.discoverer);
            this.discoverer.addListDataListener(this);
            dialog.addWindowListener(this);
            list.addListSelectionListener(this);
            joinName.addCaretListener(this);
            cancel.addActionListener(this);
            join.addActionListener(this);
            this.checkCanJoin();
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == this.join) {
                DiscoveryThread.Discovery server = (DiscoveryThread.Discovery)this.list.getSelectedValue();
                if (server != null) {
                    Network.connectToServer(server.serverAddress, this.joinName.getText());
                }
            }

            this.dialog.hide();
        }

        public void windowActivated(WindowEvent e) {
            (new Thread(this.discoverer)).start();
            this.joinName.requestFocusInWindow();
        }

        public void windowDeactivated(WindowEvent e) {
            this.joinName.setText("");
            this.discoverer.cancel();
        }

        private void checkCanJoin() {
            this.join.setEnabled(this.list.getSelectedValue() != null && this.joinName.getText().length() > 0);
        }

        public void valueChanged(ListSelectionEvent e) {
            this.checkCanJoin();
        }

        public void caretUpdate(CaretEvent e) {
            this.checkCanJoin();
        }

        public void contentsChanged(ListDataEvent e) {
        }

        public void intervalRemoved(ListDataEvent e) {
        }

        public void intervalAdded(ListDataEvent e) {
            if (this.list.getSelectedValue() == null) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        JoinListener.this.list.setSelectedIndex(0);
                    }
                });
            }

        }
    }
}
