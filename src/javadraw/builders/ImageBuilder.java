package javadraw.builders;

import javadraw.Color;
import javadraw.Image;
import javadraw.Location;
import javadraw.Screen;

public class ImageBuilder {
    private Screen screen;
    private String path;
    private Location location;
    private double width;
    private double height;
    private double rotation;
    private boolean visible;

    public ImageBuilder(Screen screen, String path, Location location) {
        this.screen = screen;
        this.path = path;
        this.location = location;
    }

    public ImageBuilder(Screen screen, String path, double x, double y) {
        this.screen = screen;
        this.path = path;
        this.location = new Location(x, y);
    }

    public ImageBuilder size(double width, double height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ImageBuilder rotation(double rotation) {
        this.rotation = rotation;
        return this;
    }

    public ImageBuilder visible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public Image build() {
        return new Image(
                screen,
                path,
                location,
                width,
                height,
                Color.BLACK,
                Color.NONE,
                true,
                rotation,
                visible
        );
    }
}
