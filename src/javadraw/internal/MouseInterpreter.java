//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MouseInterpreter implements MouseListener, MouseMotionListener {
    private Component canvas;
    private Object target;

    public MouseInterpreter(Object target, DrawingCanvas canvas) {
        this.canvas = (Component)canvas;
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
        if (this.target != null) {
            ObjectDrawObject.deferUpdates();
            Location point = new Location(event.getX(), event.getY());
            int button = event.getButton();
            String methodName = "onMouse" + type;

            try {
                Method method = this.target.getClass().getMethod(methodName, Integer.TYPE, Location.class);
                method.setAccessible(true);
                method.invoke(this.target, button, point);
            } catch (InvocationTargetException e) {
                e.getCause().printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                ObjectDrawObject.runUpdates();
            }

        }
    }

    /** @deprecated */
    public void mouseDragged(MouseEvent event) {
        this.mouseEvent(event, "Drag");
    }

    /** @deprecated */
    public void mouseMoved(MouseEvent event) {
        this.mouseEvent(event, "Move");
    }

    /** @deprecated */
    public void mouseClicked(MouseEvent event) {
        this.mouseEvent(event, "Click");
    }

    /** @deprecated */
    public void mousePressed(MouseEvent event) {
        this.canvas.requestFocus();
        this.mouseEvent(event, "Press");
    }

    /** @deprecated */
    public void mouseReleased(MouseEvent event) {
        this.mouseEvent(event, "Release");
    }

    /** @deprecated */
    public void mouseEntered(MouseEvent event) {
        this.mouseEvent(event, "Enter");
    }

    /** @deprecated */
    public void mouseExited(MouseEvent event) {
        this.mouseEvent(event, "Exit");
    }
}
