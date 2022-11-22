//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.BasicStroke;

public interface DrawableStrokeInterface {
    BasicStroke DEFAULT_STROKE = new BasicStroke(2.0F, 1, 1);
    double CLICK_SIZE = 4.0;

    double getLineWidth();

    void setLineWidth(double var1);

    BasicStroke getStroke();

    void setStroke(BasicStroke var1);
}
