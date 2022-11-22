package javadraw.internal;

import javadraw.Location;
import javadraw.MouseEventListener;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static javadraw.MouseConstants.*;

public class MouseInterpreter implements MouseListener, MouseMotionListener {
    private final HashMap<String, javadraw.internal.MouseEvent> listenerMap = new HashMap<>();
    {
        listenerMap.put(MOUSE_CLICKED, new javadraw.internal.MouseEvent());
        listenerMap.put(MOUSE_DRAGGED, new javadraw.internal.MouseEvent());
        listenerMap.put(MOUSE_ENTERED, new javadraw.internal.MouseEvent());
        listenerMap.put(MOUSE_EXITED, new javadraw.internal.MouseEvent());
        listenerMap.put(MOUSE_MOVED, new javadraw.internal.MouseEvent());
        listenerMap.put(MOUSE_PRESSED, new javadraw.internal.MouseEvent());
        listenerMap.put(MOUSE_RELEASED, new javadraw.internal.MouseEvent());
    }

    private Component canvas;
    private Object target;

    public MouseInterpreter(Object target, DrawingCanvas canvas) {
        this.canvas = (Component) canvas;
        this.canvas.addMouseListener(this);
        this.canvas.addMouseMotionListener(this);
        this.target = target;
    }

    public void removeFromCanvas() {
        if (this.canvas != null) {
            this.canvas.removeMouseListener(this);
            this.canvas.removeMouseMotionListener(this);
            this.canvas = null;
        }

    }

    public void addToCanvas(DrawingCanvas canvas) {
        this.removeFromCanvas();
        this.canvas = (Component)canvas;
        this.canvas.addMouseListener(this);
        this.canvas.addMouseMotionListener(this);
    }

    public DrawingCanvas getCanvas() {
        return (DrawingCanvas)this.canvas;
    }

    private void mouseEvent(MouseEvent event, String type) {
        if (target != null) {
            ObjectDrawObject.deferUpdates();
            Location location = new Location(event.getX(), event.getY());
            int button = event.getButton();
            String methodName = "onMouse" + type;

            listenerMap.get(type).fire(button, location);

            try {
                Method method = target.getClass().getMethod(methodName, Integer.TYPE, javadraw.Location.class);
                method.setAccessible(true);
                method.invoke(target, button, location);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException ignored) {}
        }
    }

    @Deprecated
    @Override
    public void mouseClicked(MouseEvent e) {
        mouseEvent(e, MOUSE_CLICKED);
    }

    @Deprecated
    @Override
    public void mousePressed(MouseEvent e) {
        mouseEvent(e, MOUSE_PRESSED);
    }

    @Deprecated
    @Override
    public void mouseReleased(MouseEvent e) {
        mouseEvent(e, MOUSE_RELEASED);
    }

    @Deprecated
    @Override
    public void mouseEntered(MouseEvent e) {
        mouseEvent(e, MOUSE_ENTERED);
    }

    @Deprecated
    @Override
    public void mouseExited(MouseEvent e) {
        mouseEvent(e, MOUSE_EXITED);
    }

    @Deprecated
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseEvent(e, MOUSE_DRAGGED);
    }

    @Deprecated
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseEvent(e, MOUSE_MOVED);
    }

    public void addListener(String type, MouseEventListener listener) {
        Objects.requireNonNull(listener);
        listenerMap.get(type).addListener(listener);
    }
}
