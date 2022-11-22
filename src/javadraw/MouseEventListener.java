package javadraw;

import hxasjc.event.AbstractEvent;
import hxasjc.event.Event;
import hxasjc.event.EventListener;
import javadraw.internal.MouseEvent;

public interface MouseEventListener extends EventListener {
    @Override
    default void listen(AbstractEvent<?> event, Object... objects) {
        if (event.getClass().equals(MouseEvent.class)) {
            try {
                int button = Integer.parseInt(objects[0].toString());
                Location location = (Location) objects[1];
                listen(button, location);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    void listen(int button, Location location);
}
