package javadraw;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class MultiRenderable implements DrawableObject, CustomRenderable, MultiRenderableMember {
    private final HashMap<String, MultiRenderableMember> map = new HashMap<>();
    private final HashMap<MultiRenderableMember, Offset> offsets = new HashMap<>();
    private Bounds bounds;
    private Rectangle boundsRectangle;
    private Location location = new Location(0, 0);
    private final Screen screen;

    public MultiRenderable(Screen screen) {
        this.screen = screen;
    }

    public void add(String name, MultiRenderableMember renderableMember) {
        requireNonNull(name);
        requireNonNull(renderableMember);
        map.put(name, renderableMember);
    }

    public MultiRenderableMember get(String name) {
        return map.get(name);
    }

    @Override
    public double x() {
        return location.x();
    }

    @Override
    public double y() {
        return location.y();
    }

    @Override
    public double x(double x) {
        move(Location.subtractLocations(location, new Location(x, 0)));
        return x();
    }

    @Override
    public double y(double y) {
        move(Location.subtractLocations(location, new Location(0, y)));
        return y();
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public double width() {
        return boundsRectangle.width();
    }

    @Override
    public double height() {
        return boundsRectangle.height();
    }

    @Override
    public double rotation() {
        return boundsRectangle.rotation();
    }

    @Override
    public double rotation(double v) {
        boundsRectangle.rotation(v);
        return boundsRectangle.rotation();
    }

    @Override
    public void move(double x, double y) {
        map.forEach((s, renderable) -> renderable.move(x, y));
    }

    @Override
    public void move(Location location) {
        map.forEach((s, renderable) -> renderable.move(location));
    }

    @Override
    public void moveTo(double x, double y) {
        calculateOffsets();
        boundsRectangle.moveTo(x, y);
        applyOffsets();
    }

    @Override
    public void moveTo(Location location) {
        calculateOffsets();
        boundsRectangle.moveTo(location);
        applyOffsets();
    }

    @Override
    public boolean overlaps(Renderable renderable) {
        return false;
        //TODO
    }

    @Override
    public boolean visible(boolean visible) {
        return false;
        //TODO
    }

    public void rotate(double angle) {
        calculateOffsets();
        boundsRectangle.rotate(angle);
        applyOffsets();
    }

    @Override
    public void front() {
        map.forEach((s, renderable) -> renderable.front());
    }

    @Override
    public void back() {
        map.forEach((s, renderable) -> renderable.back());
    }

    @Override
    public void remove() {
        map.forEach((s, renderable) -> renderable.remove());
    }

    public void updateBounds() {
        bounds = getNewBounds();
    }

    private Bounds getNewBounds() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = 0;
        double maxY = 0;

        for (Map.Entry<String, MultiRenderableMember> entry : map.entrySet()) {
            Location loc = entry.getValue().location();
            double x = entry.getValue().width();
            double y = entry.getValue().height();

            if (loc.x() < minX) {
                minX = loc.x();
            }
            if (loc.y() < minY) {
                minY = loc.y();
            }

            if (loc.x() + x > maxX) {
                maxX = loc.x() + x;
            }
            if (loc.y() + y > maxY) {
                maxY = loc.y() + y;
            }
        }
        return new Bounds(minX, minY, maxX, maxY);
    }

    private void drawBoundsRect() {
        updateBounds();

        if (boundsRectangle != null) {
            boundsRectangle.remove();
        }

        double sizeX = bounds.maxX() - bounds.minX();
        double sizeY = bounds.maxY() - bounds.minY();
        boundsRectangle = new Rectangle(
                screen,
                location,
                sizeX,
                sizeY
        );

        boundsRectangle.color(Color.CYAN);
        boundsRectangle.back();

        location = boundsRectangle.location;

        boundsRectangle.visible(false);
    }

    public void calculateOffsets() {
        drawBoundsRect();

        map.forEach((s, renderable) -> {
            offsets.put(renderable, new Offset(
                    Location.subtractLocations(renderable.location(), location),
                    renderable.rotation()));
        });
    }

    public void applyOffsets() {
        offsets.forEach((renderable, offset) -> {
            renderable.moveTo(Location.addLocations(boundsRectangle.location, offset.location));
            renderable.rotation(offset.rotation() + boundsRectangle.rotation());
        });

        screen.update();
    }

    public Bounds getBounds() {
        return bounds;
    }

    public int size() {
        return map.size();
    }

    public void empty() {
        remove();
        map.clear();
    }

    private record Bounds(double minX, double minY, double maxX, double maxY) {}

    private record Offset(Location location, double rotation) {}
}
