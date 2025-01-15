//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public interface Drawable1DInterface extends DrawableInterface {
    Location getStart();

    Location getEnd();

    void setStart(Location var1);

    void setStart(double var1, double var3);

    void setEnd(Location var1);

    void setEnd(double var1, double var3);

    void setEndPoints(Location var1, Location var2);

    void setEndPoints(double var1, double var3, double var5, double var7);
}
