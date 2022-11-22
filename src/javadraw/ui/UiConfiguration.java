package javadraw.ui;

import javadraw.Color;

public record UiConfiguration(
        boolean roundedBackground,
        double roundedBackgroundArcWidth,
        double roundedBackgroundArcHeight,
        Color backgroundColor
) {
    public static final UiConfiguration defaultConfiguration = new UiConfiguration(
            true,
            5,
            5,
            Color.LIGHT_GRAY
    );
}
