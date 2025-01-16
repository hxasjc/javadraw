//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.JApplet;

public class Controller extends JApplet {
    private Dimension preferredSize;

    public Controller() {
    }

    public static String getVersion() {
        return "v1.2.6";
    }

    /** @deprecated */
    public final void init() {
    }

    public void begin() {
    }

    /** @deprecated */
    public void start() {
        this.begin();
    }

    /** @deprecated */
    public void stop() {
        ActiveObject.killAll();
        EventTimer.stopAll();
    }

    /** @deprecated */
    public void startController(int width, int height) {
        this.startController(width, height, "CoffeeDraw");
    }

    void setControllerSize(Dimension d) {
        this.getContentPane().setPreferredSize(d);
    }

    public void startController(int width, int height, String name) {
        this.setControllerSize(new Dimension(width, height));
        new ControllerFrame(name, this);
    }

    public void startController() {
        this.startController(400, 400);
    }

    public AudioClip getAudio(String path) {
        return this.getAudioClip(this.getCodeBase(), path);
    }

    public Image getImage(String path) {
        return this.getImage(this.getCodeBase(), path);
    }

    static {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        } catch (Exception ignored) {
        }

    }
}
