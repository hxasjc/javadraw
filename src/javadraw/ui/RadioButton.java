package javadraw.ui;

import javadraw.*;

public class RadioButton extends ToggleButton {
    protected static final String CHECKED_BUTTON = "icons/radiobutton_yes.png";
    protected static final String UNCHECKED_BUTTON = "icons/radiobutton_no.png";

    protected Object sourceObject;
    private double yPos;

    RadioButton(Screen screen, UiConfiguration uiConfiguration, Object source, double yPos) {
        super(screen, source.toString());
        this.sourceObject = source;
        this.yPos = yPos;
    }

    @Override
    public void visualizeSelection() {
        //
    }

    @Override
    public void draw() {
        drawableObject.add("checked", new Image(screen, CHECKED_BUTTON, PADDING, yPos));
        drawableObject.add("unchecked", new Image(screen, UNCHECKED_BUTTON, PADDING, yPos));

        drawableObject.add("text", new Text(screen, sourceObject, (2 * PADDING) + IMAGE_SIZE, yPos));
    }

    public double getWidth() {
        return (3 * PADDING) + IMAGE_SIZE +
                drawableObject.get("text").width();
    }
}
