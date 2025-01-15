//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.io.Serializable;

@SuppressWarnings("unused")
public class AngLine extends Line implements Serializable {
    public AngLine(Location start, double length, double radianAngle, Color color, DrawingCanvas canvas) {
        this(start.getDoubleX(), start.getDoubleY(), length, radianAngle, color, canvas);
    }

    public AngLine(Location start, double length, double radianAngle, DrawingCanvas canvas) {
        this(start, length, radianAngle, null, canvas);
    }

    public AngLine(double x, double y, double length, double radianAngle, Color color, DrawingCanvas canvas) {
        super(x, y, x + length * Math.cos(radianAngle), y + length * Math.sin(radianAngle), color, canvas);
    }

    public AngLine(double x, double y, double length, double radianAngle, DrawingCanvas canvas) {
        this(x, y, length, radianAngle, null, canvas);
    }
}
