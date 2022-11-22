package javadraw;

import javadraw.internal.FilledPolygon;
import javadraw.internal.FramedPolygon;

import java.util.Arrays;

/**
 * A custom polygon with user-defined vertices.
 */
public class CustomPolygon extends Renderable implements CustomRenderable {

    private final Object[] parameterValues;

    private Location[] vertices;

    /**
     * Create a custom polygon
     * @param screen Screen to draw the shape on
     * @param vertices Vertices to draw the shape with
     * @param color Color to draw the shape with
     * @param border Color to draw the border with
     * @param fill Whether to fill in the shape
     * @param rotation Degrees to rotate the shape by
     * @param visible Whether the shape is visible
     */
    public CustomPolygon(Screen screen, Location[] vertices, Color color, Color border, boolean fill, double rotation, boolean visible) {
        super(screen, vertices[0], -1, -1, color, border, fill, rotation, visible);
        this.parameterValues = new Object[] {screen, vertices, color, border, fill, rotation, visible};

        this.calculateLocation(vertices);
        javadraw.internal.Location[] trueVertices = convertLocations(vertices);

        this.vertices = vertices;

        if(fill) {
            this.object = new FilledPolygon(trueVertices, location.x(), location.y(), color.toAWT(), screen.canvas);
        } else {
            this.object = new FramedPolygon(trueVertices, location.x(), location.y(), rotation, color.toAWT(), screen.canvas);
        }

        this.visible(visible);
    }

    /**
     * Create a custom polygon
     * @param screen Screen to draw the shape on
     * @param vertices Vertices to draw the shape with
     * @param color Color to draw the shape with
     * @param rotation Degrees to rotate the shape by
     */
    public CustomPolygon(Screen screen, Location[] vertices, Color color, double rotation) {
        this(screen, vertices, color, Color.NONE, true, rotation, true);
    }

    /**
     * Create a custom polygon
     * @param screen Screen to the shape on
     * @param vertices Vertices to draw the shape with
     * @param rotation Degrees to rotate the shape by
     */
    public CustomPolygon(Screen screen, Location[] vertices, double rotation) {
        this(screen, vertices, new Color("BLACK"), rotation);
    }

    /**
     * Create a custom polygon
     * @param screen Screen to draw the shape on
     * @param vertices Vertices to draw the shape with
     * @param color Color to draw the shape with
     */
    public CustomPolygon(Screen screen, Location[] vertices, Color color) {
        this(screen, vertices, color, 0);
    }

    /**
     * Create a custom polygon
     * @param screen Screen to draw the shape on
     * @param vertices Vertices to create the shape with
     */
    public CustomPolygon(Screen screen, Location[] vertices) {
        this(screen, vertices, new Color("BLACK"));
    }

    private void calculateLocation(Location[] vertices) {
        double xMin = vertices[0].x();
        double xMax = vertices[0].x();
        double yMin = vertices[0].y();
        double yMax = vertices[0].y();


        for (Location loc : vertices) {
            if (loc.x() < xMin)
                xMin = loc.x();

            if(loc.x() > xMax)
                xMax = loc.x();

            if (loc.y() < yMin)
                yMin = loc.y();

            if (loc.y() > yMax)
                yMax = loc.y();
        }

        this.location = new Location(xMin, yMin);
        this.width = xMax - xMin;
        this.height = yMax - yMin;
    }

    private javadraw.internal.Location[] convertLocations(Location[] locations) {
        javadraw.internal.Location[] newLocations = new javadraw.internal.Location[locations.length];
        for(int i = 0; i < newLocations.length; i++) {
            newLocations[i] = locations[i].location;
        }

        return newLocations;
    }

    @Override
    protected Object[] getParameters() {
        return parameterValues;
    }

    @Override
    public String toString() {
        return "CustomPolygon{" +
                "vertices=" + Arrays.toString(vertices) +
                ", screen=" + screen +
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
}
