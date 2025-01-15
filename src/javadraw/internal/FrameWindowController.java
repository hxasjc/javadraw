//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import javax.swing.SwingUtilities;

@SuppressWarnings("unused")
public class FrameWindowController extends WindowController {
    public FrameWindowController(String name) {
        this(400, 400, name);
    }

    public FrameWindowController(final int width, final int height, final String name) {
        SwingUtilities.invokeLater(() -> FrameWindowController.this.startController(width, height, name));
    }

    public FrameWindowController() {
        this(400, 400, "ObjectDraw");
    }

    public FrameWindowController(int width, int height) {
        this(width, height, "ObjectDraw");
    }
}
