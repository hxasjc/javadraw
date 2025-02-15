package javadraw;

import javadraw.internal.SneakyThrow;

import java.lang.reflect.Field;

/**
 * Represents a Color in the RGB format. Contains some presets with names.
 */
public class Color {

    /**
     * A color representing absence of color.
     */
    public static final Color NONE = new Color("NONE");

    /**
     * The color white.  In the default sRGB space.
     * @deprecated Use {@link #WHITE} instead
     */
    @Deprecated
    public final static Color white     = new Color(255, 255, 255);

    /**
     * The color white.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color WHITE = white;

    /**
     * The color light gray.  In the default sRGB space.
     * @deprecated Use {@link #LIGHT_GRAY} instead
     */
    @Deprecated
    public final static Color lightGray = new Color(192, 192, 192);

    /**
     * The color light gray.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color LIGHT_GRAY = lightGray;

    /**
     * The color gray.  In the default sRGB space.
     * @deprecated use {@link #GRAY} instead
     */
    @Deprecated
    public final static Color gray      = new Color(128, 128, 128);

    /**
     * The color gray.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color GRAY = gray;

    /**
     * The color dark gray.  In the default sRGB space.
     * @deprecated use {@link #DARK_GRAY} instead
     */
    @Deprecated
    public final static Color darkGray  = new Color(64, 64, 64);

    /**
     * The color dark gray.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color DARK_GRAY = darkGray;

    /**
     * The color black.  In the default sRGB space.
     * @deprecated Use {@link #BLACK} instead
     */
    @Deprecated
    public final static Color black     = new Color(0, 0, 0);

    /**
     * The color black.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color BLACK = black;

    /**
     * The color red.  In the default sRGB space.
     * @deprecated use {@link #RED} instead
     */
    @Deprecated
    public final static Color red       = new Color(255, 0, 0);

    /**
     * The color red.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color RED = red;

    /**
     * The color pink.  In the default sRGB space.
     * @deprecated Use {@link #PINK} instead
     */
    @Deprecated
    public final static Color pink      = new Color(255, 175, 175);

    /**
     * The color pink.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color PINK = pink;

    /**
     * The color orange.  In the default sRGB space.
     * @deprecated use {@link #ORANGE} instead
     */
    @Deprecated
    public final static Color orange    = new Color(255, 200, 0);

    /**
     * The color orange.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color ORANGE = orange;

    /**
     * The color yellow.  In the default sRGB space.
     * @deprecated use {@link #YELLOW} instead
     */
    @Deprecated
    public final static Color yellow    = new Color(255, 255, 0);

    /**
     * The color yellow.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color YELLOW = yellow;

    /**
     * The color green.  In the default sRGB space.
     * @deprecated Use {@link #GREEN} instead
     */
    @Deprecated
    public final static Color green     = new Color(0, 255, 0);

    /**
     * The color green.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color GREEN = green;

    /**
     * The color magenta.  In the default sRGB space.
     * @deprecated Use {@link #MAGENTA} instead
     */
    @Deprecated
    public final static Color magenta   = new Color(255, 0, 255);

    /**
     * The color magenta.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color MAGENTA = magenta;

    /**
     * The color cyan.  In the default sRGB space.
     * @deprecated Use {@link #CYAN} instead
     */
    @Deprecated
    public final static Color cyan      = new Color(0, 255, 255);

    /**
     * The color cyan.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color CYAN = cyan;

    /**
     * The color blue.  In the default sRGB space.
     * @deprecated Use {@link #BLUE} instead
     */
    @Deprecated
    public final static Color blue      = new Color(0, 0, 255);

    /**
     * The color blue.  In the default sRGB space.
     * @since 1.4
     */
    public final static Color BLUE = blue;

    protected java.awt.Color color;

    private int r, g, b;

    /**
     * Create a new Color based on a String name.
     * @param name name of the Color
     */
    public Color(String name) {
        if(name.equalsIgnoreCase("none")) {
            color = null;
            return;
        }

        try {
            Field field = java.awt.Color.class.getField(name);
            color = (java.awt.Color) field.get(null);
        } catch (Exception e) {
            color = null;
            SneakyThrow.sneakyThrow(new IllegalArgumentException("Invalid color name passed!"));
        }

        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
    }

    public Color(int r, int g, int b) {
        if(r > 255 || g > 255 || b > 255 ||
            r < 0 || g < 0 || b < 0) {
            SneakyThrow.sneakyThrow(new IllegalArgumentException("Invalid RGB values passed to Color! (0-255 only)"));
        }

        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Duplicate a color
     * @param color Color to duplicate
     */
    public Color(Color color) {
        this.r = color.red();
        this.g = color.blue();
        this.b = color.blue();
    }

    /**
     * Create a new Color
     *
     * @param color
     */
    protected Color(java.awt.Color color) {
        this.color = color;
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
    }

    /**
     * Get the red value of the Color
     * @return an integer (0 - 255)
     */
    public int red() {
        return this.r;
    }

    /**
     * Get the green value of the color
     * @return an integer (0 - 255)
     */
    public int green() {
        return this.g;
    }

    /**
     * Get the blue value of the Color
     * @return an integer (0 - 255)
     */
    public int blue() {
        return this.b;
    }

    /**
     * Get a random Color (entirely random)
     * @return a random Color (random red, green, and blue values)
     */
    public static Color random() {
        return new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    protected java.awt.Color toAWT() {
        return new java.awt.Color(this.red(), this.green(), this.blue());
    }
}
