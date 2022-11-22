package javadraw.ui;

import hxasjc.event.AbstractEvent;
import hxasjc.event.EventListener;
import javadraw.MouseTools;
import javadraw.Screen;
import javadraw.errors.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ToggleButton extends Component {
    protected String name;
    protected String description;
    protected boolean selected;

    private static ArrayList<ToggleButton> buttons = new ArrayList<>();
    private static MouseTools MOUSE_TOOLS;

    private final SelectionEvent selectionEvent = new SelectionEvent();

    public ToggleButton(Screen screen, String text, boolean selected, boolean useCustomClickHandler) {
        super(screen);
        this.description = text;
        this.selected = selected;
        buttons.add(this);
        //drawNew();
        visualizeSelection();

        if (MOUSE_TOOLS == null) {
            MOUSE_TOOLS = new MouseTools(screen.getWindow());
            MOUSE_TOOLS.setMouseTrackingEnabled(true);
        }

        if (!useCustomClickHandler) {
            MOUSE_TOOLS.subscribe(ToggleButton::handleClick);
        }
    }

    public ToggleButton(Screen screen, String text) {
        this(screen, text, false, false);
    }

    public boolean getSelected() {
        return selected;
    }

    protected void setSelected(boolean selected) {
        this.selected = selected;
        selectionEvent.fire(this, selected);
        visualizeSelection();
    }

    protected void toggleSelection() {
        setSelected(!getSelected());
    }

    public abstract void visualizeSelection();

    public void addSelectionListener(SelectionEventListener listener) {
        selectionEvent.addListener(listener);
    }

    public interface SelectionEventListener extends EventListener {
        @Override
        default void listen(AbstractEvent<?> abstractEvent, Object... objects) {
            throw new NotImplementedException();
        }

        void listen(ToggleButton component, boolean selection);
    }

    private static class SelectionEvent extends AbstractEvent<SelectionEventListener> {
        @Override
        public void fire(Object... args) {
            throw new NotImplementedException();
        }

        public void fire(ToggleButton component, boolean selection) {
            listeners.forEach(listener -> {
                listener.listen(component, selection);
            });
        }
    }

    //public abstract void drawNew();

    @Override
    public void draw() {
        //
    }

    public void forgetButton(ToggleButton button) {
        buttons.remove(button);
    }

    public static void handleClick(AbstractEvent<?> event, Object[] objects) {
        System.out.println(objects.length);
        System.out.println(Arrays.toString(objects));
    }
}
