//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Dimension;
import javax.swing.JPanel;

public class SizeablePanel extends JPanel {
    private int width;
    private int height;

    public SizeablePanel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Dimension getPreferredSize() {
        return new Dimension(this.width, this.height);
    }

    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }
}
