package javadraw.ui;

import javadraw.*;
import javadraw.errors.NotImplementedException;

import javax.swing.*;

public abstract class Component implements DrawableObject, CustomRenderable, MultiRenderableMember {
    protected static final double IMAGE_SIZE = 16;
    protected static final int PADDING = 7;

    protected MultiRenderable drawableObject;
    protected Screen screen;
    protected UiConfiguration uiConfiguration;

    public Component(Screen screen) {
        this(screen, UiConfiguration.defaultConfiguration);
    }

    public Component(Screen screen, UiConfiguration uiConfiguration) {
        this.screen = screen;
        this.uiConfiguration = uiConfiguration;
        draw();
    }

    public abstract void draw();

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected ImageIcon createImageIcon(String path,
                                        String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void initFire() {}

    @Override
    public boolean overlaps(Renderable renderable) {
        return drawableObject.overlaps(renderable);
    }

    @Override
    public boolean visible(boolean visible) {
        return drawableObject.visible(visible);
    }

    @Override
    public double width() {
        return drawableObject.width();
    }

    @Override
    public double height() {
        return drawableObject.height();
    }

    @Override
    public double rotation() {
        return drawableObject.rotation();
    }

    @Override
    public double rotation(double v) {
        return drawableObject.rotation(v);
    }

    @Override
    public double x() {
        return drawableObject.x();
    }

    @Override
    public double y() {
        return drawableObject.y();
    }

    @Override
    public double x(double x) {
        return drawableObject.x(x);
    }

    @Override
    public double y(double y) {
        return drawableObject.y(y);
    }

    @Override
    public Location location() {
        return drawableObject.location();
    }

    @Override
    public void move(double dx, double dy) {
        drawableObject.move(dx, dy);
    }

    @Override
    public void move(Location location) {
        drawableObject.move(location);
    }

    @Override
    public void moveTo(double x, double y) {
        drawableObject.moveTo(x, y);
    }

    @Override
    public void moveTo(Location location) {
        drawableObject.moveTo(location);
    }

    @Override
    public void front() {
        drawableObject.front();
    }

    @Override
    public void back() {
        drawableObject.back();
    }

    @Override
    public void remove() {
        drawableObject.remove();
    }
}
