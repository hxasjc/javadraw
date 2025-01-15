//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import javax.swing.SwingUtilities;

public class FrameController extends Controller {
    public FrameController() {
        this(400, 400, "ObjectDraw");
    }

    public FrameController(int width, int height) {
        this(width, height, "ObjectDraw");
    }

    public FrameController(String name) {
        this(400, 400, name);
    }

    public FrameController(final int width, final int height, final String name) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                FrameController.this.startController(width, height, name);
            }
        });
    }
}
