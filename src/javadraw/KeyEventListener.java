package javadraw;

import hxasjc.event.AbstractEvent;
import hxasjc.event.Event;
import hxasjc.event.EventListener;
import javadraw.internal.KeyEvent;

/**
 * Specialization of {@link EventListener} that provides the values in a more user-friendly way. Handle actions when an event regarding a key is fired. Passed to {@link Window#addKeyEventListener(String, KeyEventListener)}.
 */
public interface KeyEventListener extends EventListener {
    @Override
    default void listen(AbstractEvent<?> event, Object... objects) {
        if (event.getClass().equals(KeyEvent.class)) {
            try {
                String key = (String) objects[0];
                listen(Key.fromChar(key));
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    //void listen(String key);

    /**
     * Passes the Key that was pressed/released
     * @param key Key that the event was fired by
     */
    void listen(Key key);
}
