//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/** @deprecated */
public class ControllerFrame extends JFrame implements AppletStub, AppletContext {
    private static final ArrayList<ControllerFrame> frames = new ArrayList<>();
    private static boolean showingRulers = false;
    private static boolean showingGrids = false;
    private static final Color RULER_COLOR = new Color(50, 50, 50);
    private static final Color RULER_BACKGROUND = new Color(200, 200, 200);
    private static final Font RULER_FONT = Font.decode("Times-BOLD-12");
    private static final Color TICK_COLOR;
    private Controller applet;
    private boolean active = false;
    private final HashMap<String, InputStream> streams = new HashMap<>();
    private int width;
    private int height;
    private MouseInterpreter key;
    private JCheckBoxMenuItem rulersItem;
    private JCheckBoxMenuItem gridItem;
    private File lastScreenshotDir = null;

    private void showRulers() {
        ((WindowController)this.applet).setRulersVisible(true);
        this.pack();
    }

    private void hideRulers() {
        ((WindowController)this.applet).setRulersVisible(false);
        this.pack();
    }

    public static void setRulers(boolean show) {
        if (showingRulers != show) {
            showingRulers = show;

            for (ControllerFrame controllerFrame : frames) {
                ControllerFrame frame = (ControllerFrame) controllerFrame;
                if (frame.applet instanceof WindowController) {
                    if (show) {
                        frame.showRulers();
                    } else {
                        frame.hideRulers();
                    }

                    frame.rulersItem.setSelected(show);
                }
            }

        }
    }

    public static void setGrids(boolean show) {
        if (showingGrids != show) {
            showingGrids = show;

            for (ControllerFrame controllerFrame : frames) {
                ControllerFrame frame = (ControllerFrame) controllerFrame;
                if (frame.applet instanceof WindowController) {
                    WindowController controller = (WindowController) frame.applet;
                    if (show) {
                        controller.setGridVisible(true);
                    } else {
                        controller.setGridVisible(false);
                    }

                    frame.gridItem.setSelected(show);
                }
            }

        }
    }

    public ControllerFrame(String title, Controller myController) {
        super(title);
        this.applet = myController;
        this.getContentPane().add(this.applet, "Center");
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.buildMenus();
        this.addWindowListener(new WindowAdapter() {
            public void windowActivated(WindowEvent e) {
                if (ControllerFrame.this.applet instanceof WindowController) {
                    ((WindowController)ControllerFrame.this.applet).canvas.requestFocusInWindow();
                }

            }

            public void windowClosed(WindowEvent e) {
                ControllerFrame.this.active = false;
                ControllerFrame.this.applet.stop();
                ControllerFrame.this.applet.destroy();
                System.exit(0);
            }
        });
        this.applet.setStub(this);
        this.active = true;
        frames.add(this);
        Network.frameCreated(this);
        SwingUtilities.invokeLater(() -> {
            ControllerFrame.this.pack();
            ControllerFrame.this.setVisible(true);
            ControllerFrame.this.applet.init();
            ControllerFrame.this.applet.start();
        });
    }

    JMenuBar getControllerMenubar() {
        JMenuBar appletMenu = this.applet.getJMenuBar();
        return appletMenu == null ? this.getJMenuBar() : appletMenu;
    }

    private void buildMenus() {
        boolean mac = false;

        try {
            mac = System.getProperty("os.name").contains("Mac");
        } catch (SecurityException ignored) {
        }

        JMenuBar menuBar = new JMenuBar();
        if (!mac) {
            JMenu menu = new JMenu("File");
            menuBar.add(menu);

            JMenuItem screenshot = new JMenuItem("Take Screenshot");
            screenshot.addActionListener(e -> {
                WindowController controller = (WindowController) this.applet;

                JFileChooser fileChooser = new JFileChooser();

                if (lastScreenshotDir != null) {
                    fileChooser.setCurrentDirectory(lastScreenshotDir);
                }

                fileChooser.setSelectedFile(new File(fileChooser.getCurrentDirectory(), this.getTitle() + "-screenshot.png"));

                fileChooser.setDialogTitle("Save a Screenshot");

                fileChooser.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Files", "png");
                fileChooser.addChoosableFileFilter(filter);

                int result = fileChooser.showSaveDialog(null);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File dir = fileChooser.getSelectedFile();
                    lastScreenshotDir = dir;
                    controller.getCanvas().screenshot(
                            dir,
                            throwable -> JOptionPane.showMessageDialog(fileChooser, "Insufficient permissions to create a file. Please try again later", "Error creating file", JOptionPane.ERROR_MESSAGE)
                    );
                }
            });
            menu.add(screenshot);

            JMenuItem quit = new JMenuItem("Exit");
            quit.addActionListener(e -> {
                ControllerFrame.this.active = false;
                ControllerFrame.this.applet.stop();
                ControllerFrame.this.applet.destroy();
                ControllerFrame.this.hide();
                ControllerFrame.this.dispose();
                System.exit(0);
            });
            menu.add(quit);
        }

        if (this.applet instanceof WindowController) {
            JMenu viewMenu = new JMenu("View");
            menuBar.add(viewMenu);
            this.rulersItem = new JCheckBoxMenuItem("Show Rulers", showingRulers);
            this.rulersItem.addActionListener(e -> ControllerFrame.setRulers(!ControllerFrame.showingRulers));
            viewMenu.add(this.rulersItem);
            this.gridItem = new JCheckBoxMenuItem("Show Grid", showingGrids);
            this.gridItem.addActionListener(e -> ControllerFrame.setGrids(!ControllerFrame.showingGrids));
            viewMenu.add(this.gridItem);
            ((WindowController)this.applet).setGridVisible(showingGrids);
            ((WindowController)this.applet).setRulersVisible(showingRulers);
        }

        if (mac) {
            this.setJMenuBar(menuBar);
        } else {
            this.applet.setJMenuBar(menuBar);
        }

    }

    public boolean isActive() {
        return this.active;
    }

    public void appletResize(int width, int height) {
        this.pack();
    }

    public AppletContext getAppletContext() {
        return this;
    }

    public String getParameter(String name) {
        return null;
    }

    public URL getCodeBase() {
        try {
            return (new File(".")).toURI().toURL();
        } catch (MalformedURLException var2) {
            return null;
        }
    }

    public URL getDocumentBase() {
        return this.getCodeBase();
    }

    public Applet getApplet(String name) {
        return this.applet;
    }

    public Enumeration<Applet> getApplets() {
        return new AppletEnumerator();
    }

    public AudioClip getAudioClip(URL url) {
        return Applet.newAudioClip(url);
    }

    public Image getImage(URL url) {
        return Toolkit.getDefaultToolkit().getImage(url);
    }

    public InputStream getStream(String key) {
        return (InputStream)this.streams.get(key);
    }

    public Iterator<String> getStreamKeys() {
        return this.streams.keySet().iterator();
    }

    public void setStream(String key, InputStream stream) {
        this.streams.put(key, stream);
    }

    public void showDocument(URL url) {
    }

    public void showDocument(URL url, String target) {
    }

    public void showStatus(String status) {
    }

    static {
        TICK_COLOR = Color.BLUE;
        Preferences prefs = Preferences.userRoot().node("/com/featuredspace/coffeedraw");
        String p = prefs.get("view", "");
        showingRulers = p.contains("R");
        showingGrids = p.contains("G");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Preferences prefs1 = Preferences.userRoot().node("/com/featuredspace/coffeedraw");
            prefs1.put("view", ControllerFrame.showingRulers ? (ControllerFrame.showingGrids ? "RG" : "R") : (ControllerFrame.showingGrids ? "G" : ""));

            try {
                prefs1.flush();
            } catch (BackingStoreException ignored) {
            }

        }));
    }

    private static class AppletEnumerator implements Enumeration<Applet> {
        private ArrayList<ControllerFrame> framesCopy;
        private int index;

        public AppletEnumerator() {
            this.framesCopy = new ArrayList<>(ControllerFrame.frames);
            this.index = 0;
        }

        public Controller nextElement() {
            return this.hasMoreElements() ? ControllerFrame.frames.get(this.index++).applet : null;
        }

        public boolean hasMoreElements() {
            return this.index < this.framesCopy.size();
        }
    }
}
