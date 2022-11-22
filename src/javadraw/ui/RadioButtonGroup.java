package javadraw.ui;

import javadraw.Location;
import javadraw.Renderable;
import javadraw.Screen;

import java.util.ArrayList;
import java.util.Collection;

public class RadioButtonGroup extends Component {
    protected Collection<?> objects;
    protected ArrayList<RadioButton> buttons = new ArrayList<>();
    private double widestButton = 0;

    public RadioButtonGroup(Screen screen, Collection<?> collection) {
        this(screen, collection, UiConfiguration.defaultConfiguration);
    }

    public RadioButtonGroup(Screen screen, Collection<?> collection, UiConfiguration uiConfiguration) {
        super(screen, uiConfiguration);
        this.objects = collection;
    }

    @Override
    public void draw() {
        int yPos = PADDING;
        int buttonId = 0;

        for (Object obj : objects) {
            RadioButton button = new RadioButton(screen, uiConfiguration, obj, yPos);
            buttons.add(button);
            drawableObject.add("button" + buttonId++, button);

            yPos += PADDING + IMAGE_SIZE;

            if (button.getWidth() > widestButton) {
                widestButton = button.getWidth();
            }
        }
    }
}
