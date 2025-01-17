package javadraw;

/**
 * Represents the "Transform" of an Object. It's "width", "height", and "angle" (or rotation).
 */
public class Transform {

    protected double width, height, angle;

    protected Transform(double width, double height, double angle) {
        this.width = width;
        this.height = height;
        this.angle = angle;
    }

    /**
     * Get the width of the Transform
     * @return a double representing the width
     */
    public double width() {
        return this.width;
    }

    /**
     * Get the height of the Transform
     * @return a double representing the height
     */
    public double height() {
        return this.height;
    }

    /**
     * Get the angle/rotation of the Transform
     * @return a double representing the angle
     */
    public double angle() {
        return this.angle;
    }

}
