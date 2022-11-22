package javadraw;

import javadraw.internal.VisibleImage;

/**
 * Represents an image rendered onto the Screen. Supported formats include (but aren't limited to):
 *      - PNG
 *      - JPG
 *      - GIF
 *      - and more...
 */
public class Image extends Renderable implements CustomRenderable {
    private final Object[] parameterValues;

    private final String path;

    /**
     * Creates a new Image
     * @param screen Screen to draw the image on
     * @param path Path to the image
     * @param location Location to draw the Image at
     * @param width Width to draw the Image with
     * @param height Height to draw the Image with
     * @param color Has no effect on Images
     * @param border Color to draw the border with
     * @param fill Has no effect on Images
     * @param rotation Degrees to rotate the Image by
     * @param visible Whether the Image should be visible
     */
    public Image(Screen screen, String path, Location location, double width, double height, Color color, Color border, boolean fill, double rotation, boolean visible) {
        super(screen, location, width, height, color, border, fill, rotation, visible);
        this.parameterValues = new Object[] {screen, path, location, width, height, color, border, fill, rotation, visible};

        this.path = path;

        this.object = new VisibleImage(screen.window.controller.getImage(path), location.location, width, height, screen.canvas);

        visible(visible);
    }

    /**
     * Creates a new Image
     * @param screen Screen to draw the image on
     * @param path Path to the image
     * @param location Location to draw the Image at
     * @param color Has no effect on Images
     * @param border Color to draw the border with
     * @param fill Has no effect on Images
     * @param rotation Degrees to rotate the Image by
     * @param visible Whether the Image should be visible
     */
    public Image(Screen screen, String path, Location location, Color color, Color border, boolean fill, double rotation, boolean visible) {
        super(screen, location, -1, -1, color, border, fill, rotation, visible);
        this.parameterValues = new Object[] {screen, path, location, color, border, fill, rotation, visible};

        this.path = path;

        this.object = new VisibleImage(screen.window.controller.getImage(path), location.location, screen.canvas);

        // Grab the width and height of the loaded image.
        this.width = this.object.getDoubleWidth();
        this.height = this.object.getDoubleHeight();
    }

    /**
     * Creates a new Image
     * @param screen Screen to draw the image on
     * @param path Path to the image
     * @param x X coordinate to draw the image at
     * @param y Y coordinate to draw the image at
     */
    public Image(Screen screen, String path, double x, double y) {
        this(screen, path, new Location(x, y), Color.NONE, Color.NONE, false, 0, true);
    }

    /**
     * Creates a new Image
     * @param screen Screen to draw the image on
     * @param path Path to the image
     * @param x X coordinate to draw the image at
     * @param y Y coordinate to draw the image at
     * @param width Width to draw the Image with
     * @param height Height to draw the Image with
     */
    public Image(Screen screen, String path, double x, double y, double width, double height) {
        this(screen, path, new Location(x, y), width, height, Color.NONE, Color.NONE, false, 0, true);
    }

    /**
     * Creates a new Image
     * @param screen Screen to draw the image on
     * @param path Path to the image
     * @param location Location to draw the Image at
     */
    public Image(Screen screen, String path, Location location) {
        this(screen, path, location.x(), location.y());
    }

    /**
     * Creates a new Image
     * @param screen Screen to draw the image on
     * @param path Path to the image
     * @param location Location to draw the Image at
     * @param width Width to draw the Image with
     * @param height Height to draw the Image with
     */
    public Image(Screen screen, String path, Location location, double width, double height) {
        this(screen, path, location.x(), location.y(), width, height);
    }

    /**
     * Get the path registered to this Image.
     * @return a String representing the relative/absolute path originally passed to the constructor.
     */
    public String path() {
        return this.path;
    }

    @Override
    protected Object[] getParameters() {
        return this.parameterValues;
    }

    @Override
    public String toString() {
        return "Image{" +
                "path='" + path + '\'' +
                //", screen=" + screen +
                ", location=" + location +
                ", width=" + width +
                ", height=" + height +
                ", angle=" + angle +
                ", color=" + color +
                ", borderColor=" + borderColor +
                ", fill=" + fill +
                ", visible=" + visible +
                ", object=" + object +
                '}';
    }

    public static void initFire() {}
}
