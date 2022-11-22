package javadraw.builders;

import javadraw.Color;
import javadraw.Location;
import javadraw.Rectangle;
import javadraw.Screen;

import static java.util.Objects.requireNonNull;

public class RectangleBuilder {
    private Screen screen;
    private Location location;
    private double width;
    private double height;
    private Color color = Color.BLACK;
    private double rotation = 0;
    private Color border = Color.NONE;
    private boolean fill;
    private boolean visible;

    public RectangleBuilder(Screen screen, Location location, double width, double height) {
        this.screen = requireNonNull(screen);
        this.location = requireNonNull(location);
        this.width = width;
        this.height = height;
    }

    public RectangleBuilder(Screen screen, double x, double y, double width, double height) {
        this.screen = requireNonNull(screen);
        this.location = new Location(x, y);
        this.width = width;
        this.height = height;
    }

    public RectangleBuilder color(Color color) {
        this.color = requireNonNull(color);
        return this;
    }

    public RectangleBuilder rotation(double rotation) {
        this.rotation = rotation;
        return this;
    }

    public RectangleBuilder border(Color border) {
        this.border = requireNonNull(border);
        return this;
    }

    public RectangleBuilder fill(boolean fill) {
        this.fill = fill;
        return this;
    }

    public RectangleBuilder visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public Rectangle build() {
        return new Rectangle(screen, location, width, height, color, border, fill, rotation, visible);
    }
}
