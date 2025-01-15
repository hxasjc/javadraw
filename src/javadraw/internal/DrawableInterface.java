//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

public interface DrawableInterface {
    void hide();

    void show();

    boolean isHidden();

    void addToCanvas(DrawingCanvas var1);

    void removeFromCanvas();

    DrawingCanvas getCanvas();

    void moveTo(Location var1);

    void moveTo(double var1, double var3);

    void move(double var1, double var3);

    Color getColor();

    void setColor(Color var1);

    void sendForward();

    void sendBackward();

    void sendToFront();

    void sendToBack();

    boolean contains(Location var1);

    /** @deprecated */
    void draw(Graphics2D var1);

    void rotate(double var1);

    double getRotation();

    void setRotation(double var1);

    /** @deprecated */
    Shape getShape();
}
