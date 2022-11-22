package javadraw;

import javadraw.internal.FilledRoundedRect;
import javadraw.internal.FramedRoundedRect;

public class RoundedRectangle extends Renderable {
    private final Object[] parameterValues;

    public RoundedRectangle(Screen screen, Location location, double width, double height, double arcWidth, double arcHeight, Color color, Color borderColor, boolean fill, double rotation, boolean visible) {
        super(screen, location, width, height, color, borderColor, fill, rotation, visible);
        parameterValues = new Object[] {screen, location, width,  height, color, borderColor, fill, rotation, visible};

        if (fill) {
            object = new FilledRoundedRect(location.x(), location.y(), width, height, arcWidth, arcHeight, color.toAWT(), screen.canvas);
        } else {
            object = new FramedRoundedRect(location.x(), location.y(), width, height, arcWidth, arcHeight, color.toAWT(), screen.canvas);
        }

        this.visible(visible);
    }

    @Override
    protected Object[] getParameters() {
        return parameterValues;
    }
}
