package javadraw;

public interface MultiRenderableMember {
    void move(double x, double y);

    void move(Location location);

    void front();

    void back();

    void remove();

    Location location();

    double width();

    double height();

    double rotation();

    double rotation(double v);

    void moveTo(Location addLocations);

    boolean overlaps(Renderable renderable);

    boolean visible(boolean visible);
}
