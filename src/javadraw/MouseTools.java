package javadraw;

import hxasjc.event.Event;
import hxasjc.event.EventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javadraw.MouseConstants.*;

public class MouseTools {
    private final Window window;
    private final Event event = new Event();
    private Renderable mouseTracker = null;

    public MouseTools(Window window) {
        this.window = window;

        window.addMouseEventListener(MOUSE_CLICKED, (button, location) -> {
            if (mouseTracker != null) {
                Set<Renderable> list = new HashSet<>(List.of(window.screen.getOverlappingObjects(mouseTracker)));
                /*//System.out.println(list.remove(mouseTracker));
                //System.out.println(list.contains(mouseTracker));
                list.removeIf(o -> {
                    if (mouseTracker.toString().equals(o.toString())) {
                        System.out.println(o.toString());
                        return true;
                    }
                    return mouseTracker.equals(o);
                });
                System.out.println(list);
                if (list.isEmpty()) {
                    event.fire();
                } else {
                    event.fire(list.toArray());
                }*/
                list.removeIf(o -> o instanceof TrackingRectangle);
                list.removeIf(o -> o.location().equals(location));
                event.fire(list.toArray());
                //event.fire((Object[]) window.screen.getOverlappingObjects(mouseTracker));
            }
        });

        window.addMouseEventListener(MOUSE_MOVED, (button, location) -> {
            if (mouseTracker != null) {
                mouseTracker.center(location);
            }
        });
    }

    public void subscribe(EventListener listener) {
        event.addListener(listener);
    }

    public boolean isMouseTrackingEnabled() {
        return mouseTracker != null;
    }

    public void setMouseTrackingEnabled(boolean mouseTrackingEnabled) {
        if (mouseTrackingEnabled) {
            mouseTracker = new TrackingRectangle(window.screen, 0, 0, 2, 2);
            mouseTracker.visible(false);
        } else {
            mouseTracker = null;
        }
    }

    private static class TrackingRectangle extends Rectangle {
        public TrackingRectangle(Screen screen, double x, double y, double width, double height) {
            super(screen, x, y, width, height);
        }
    }
}
