//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.geom.Rectangle2D;

public interface Drawable2DInterface extends LocatableInterface {
    int getWidth();

    int getHeight();

    double getDoubleWidth();

    double getDoubleHeight();

    boolean overlaps(Drawable2DInterface var1);

    /** @deprecated */
    Rectangle2D getBounds();
}
