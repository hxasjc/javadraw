package javadraw.ui;

import javadraw.*;

public class CheckBox extends ToggleButton {
    private static final Location CHECKBOX_OFFSET = new Location(0, 0);

    public void drawNew() {
        drawableObject = new MultiRenderable(screen);
        drawableObject.add("checked", new Image(screen, "icons/checkbox_yes.png", Location.addLocations(CHECKBOX_OFFSET, new Location(PADDING, PADDING))));
        drawableObject.add("unchecked", new Image(screen, "icons/checkbox_no.png", Location.addLocations(CHECKBOX_OFFSET, new Location(PADDING, PADDING))));

        drawableObject.add("text", new Text(screen, description, new Location((2 * PADDING) + IMAGE_SIZE, PADDING)));
        System.out.println(drawableObject.get("text").width());
        double textWidth = drawableObject.get("text").width();

        System.out.println("hi");
        Renderable background;
        System.out.println("hi");

        if (uiConfiguration.roundedBackground()) {
            System.out.println("hi");
            background = new RoundedRectangle(
                    screen,
                    new Location(0, 0),
                    (3 * PADDING) + IMAGE_SIZE + textWidth,
                    (2 * PADDING + IMAGE_SIZE),
                    uiConfiguration.roundedBackgroundArcWidth(),
                    uiConfiguration.roundedBackgroundArcHeight(),
                    uiConfiguration.backgroundColor(),
                    Color.NONE,
                    true,
                    0,
                    true);
        } else {
            background = new Rectangle(
                    screen,
                    new Location(0, 0),
                    60,
                    15
            );
        }
        System.out.println("hi");

        background.back();
        drawableObject.add("background", background);

        screen.update();
    }

    public CheckBox(Screen screen, String text, boolean selected, boolean useCustomClickHandler) {
        super(screen, text, selected, useCustomClickHandler);
        drawNew();
        visualizeSelection();

        Image.initFire();
        Component.initFire();

        registerEventListeners();
    }

    public CheckBox(Screen screen, String text) {
        this(screen, text, false, false);
    }

    private void registerEventListeners() {
        screen.getWindow().addMouseEventListener(MouseConstants.MOUSE_CLICKED, (button, location) -> {
            if (button == MouseConstants.MOUSE_BUTTON_1) {
                Rectangle temp = new Rectangle(
                        screen,
                        location,
                        1,
                        1
                );
                boolean overlapsChecked = drawableObject.get("checked").overlaps(temp);
                if (overlapsChecked) {
                    toggleSelection();
                }
                temp.remove();
            }
        });
    }

    @Override
    public void visualizeSelection() {
        try {
            drawableObject.get("checked").visible(selected);
            drawableObject.get("unchecked").visible(!selected);
        } catch (NullPointerException ignored) {}
    }
}
