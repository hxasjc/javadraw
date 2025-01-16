//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/** @deprecated */
public abstract class ObjectDrawShape extends ObjectDrawObject implements DrawableInterface, Serializable {
    private Color color;
    private boolean filled;
    private boolean hidden;
    private DrawingCanvas canvas;
    private Location location;
    private Shape shape;
    private boolean ready;
    private double angle;
    public boolean ROTATE_FLAG = true;
    static final Color DEFAULT_COLOR;
    private static HashMap<DrawingCanvas, ArrayList<DrawableInterface>> canvases;

    public ObjectDrawShape(Location l, boolean f, Color c, DrawingCanvas canvas) {
        this.filled = f;
        this.hidden = false;
        this.setMyLocation(l);
        this.color = c == null ? DEFAULT_COLOR : c;
        this.shape = null;
        this.canvas = canvas;
        this.ready = false;
    }

    public ObjectDrawShape(Location l, double rotation, boolean f, Color c, DrawingCanvas canvas) {
        this.filled = f;
        this.hidden = false;
        this.setMyLocation(l);
        this.color = c == null ? DEFAULT_COLOR : c;
        this.shape = null;
        this.ROTATE_FLAG = true;
        this.angle = rotation;
        this.canvas = canvas;
        this.ready = false;
    }

    /** @deprecated */
    protected synchronized void ready() {
        if (!this.ready) {
            this.ready = true;
            if (this.canvas != null) {
                ArrayList<DrawableInterface> list = canvases.computeIfAbsent(this.canvas, k -> new ArrayList<>());

                list.add(this);
                this.depend(this.canvas);
                this.update();
            }
        }
    }

    /** @deprecated */
    protected void setMyLocation(Location loc) {
        if (this.location != null) {
            this.location.undepend(this);
        }

        this.location = loc;
        if (this.location != null) {
            this.location.depend(this);
        }

    }

    /** @deprecated */
    public Shape getShape() {
        ObjectDrawObject.runUpdates();
        if (this.shape != null) {
            return this.shape;
        } else {
            this.shape = this.makeShape();
            return this.shape;
        }
    }

    /** @deprecated */
    public void setMyShape(Shape shape) {
        this.shape = shape;
    }

    /** @deprecated */
    public abstract Shape makeShape();

    /** @deprecated */
    public void update() {
        this.shape = null;
        super.update();
    }

    /** @deprecated */
    protected Location getMyLocation() {
        return this.location;
    }

    /** @deprecated */
    public static DrawableIterator getDrawables(DrawingCanvas canvas) {
        return new DrawableIterator((ArrayList<DrawableInterface>)canvases.get(canvas));
    }

    /** @deprecated */
    public static void draw(DrawingCanvas canvas, Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setColor(canvas.getBackground());
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableIterator d = getDrawables(canvas);

        while(d.hasNext()) {
            DrawableInterface di = d.next();
            if (di != null && !di.isHidden()) {
                try {
                    di.draw(g);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /** @deprecated */
    public static void draw(DrawingCanvas canvas, Graphics2D g, double angle) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setColor(canvas.getBackground());
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableIterator d = getDrawables(canvas);

        while(d.hasNext()) {
            DrawableInterface di = d.next();
            if (di != null && !di.isHidden()) {
                try {
                    di.draw(g);
                    di.setRotation(di.getRotation());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /** @deprecated */
    public void draw(Graphics2D g) {
        if (this.ROTATE_FLAG) {
            g = (Graphics2D)g.create();
        }

        g.setColor(this.color);
        Shape s = this.getShape();
        if (s != null) {
            if (this.ROTATE_FLAG) {
                this.internalRotate(g);
            }

            if (this.filled) {
                g.fill(s);
            } else {
                if (this instanceof DrawableStrokeInterface) {
                    g.setStroke(((DrawableStrokeInterface)this).getStroke());
                }

                if (s instanceof Polygon) {
                    g.drawPolygon((Polygon)s);
                } else {
                    g.draw(s);
                }
            }

            if (this.ROTATE_FLAG) {
                g.dispose();
            }

        }
    }

    /** @deprecated */
    public void internalRotate(Graphics2D g) {
        if (this.shape != null && this.location != null && g != null) {
            try {
                g.rotate(Math.toRadians(this.angle), this.location.getDoubleX() + this.shape.getBounds2D().getWidth() / (double)2.0F, this.location.getDoubleY() + this.shape.getBounds2D().getHeight() / (double)2.0F);
            } catch (NullPointerException ignored) {
            }
        }

    }

    /** @deprecated */
    public static void staticRotate(Graphics2D g, double angle) {
        g.rotate(Math.toRadians(angle));
    }

    /** @deprecated */
    public void setFilled(boolean filled) {
        this.filled = filled;
        this.update();
    }

    /** @deprecated */
    public static void clearCanvas(DrawingCanvas canvas) {
        DrawableIterator d = getDrawables(canvas);

        while(d.hasNext()) {
            ObjectDrawShape di = (ObjectDrawShape)d.next();
            di.setCanvas(null);
        }

    }

    /** @deprecated */
    public static void removeCanvas(DrawingCanvas canvas) {
        clearCanvas(canvas);
        canvases.remove(canvas);
    }

    /** @deprecated */
    private void setCanvas(DrawingCanvas c) {
        if (this.canvas != null) {
            this.undepend(this.canvas);
            ArrayList<DrawableInterface> list = canvases.get(this.canvas);
            if (list != null) {
                list.remove(this);
            }

            this.canvas.update();
        }

        if (c != null) {
            ArrayList<DrawableInterface> list = canvases.computeIfAbsent(c, k -> new ArrayList<>());

            list.add(this);
            this.depend(c);
        }

        this.canvas = c;
        this.update();
    }

    public void addToCanvas(DrawingCanvas c) {
        if (this.canvas != null) {
            throw new IllegalStateException("An object that is already on a canvas cannot be added to another.");
        } else {
            this.setCanvas(c);
        }
    }

    public DrawingCanvas getCanvas() {
        return this.canvas;
    }

    public Color getColor() {
        return this.color;
    }

    public void hide() {
        this.hidden = true;
        this.update();
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public void move(double dx, double dy) {
        this.location.translate(dx, dy);
    }

    public void moveTo(double x, double y) {
        this.move(x - this.location.getDoubleX(), y - this.location.getDoubleY());
    }

    public void moveTo(Location point) {
        this.moveTo(point.getDoubleX(), point.getDoubleY());
    }

    public void rotate(double toRotate) {
        if (this.canvas != null) {
            this.ROTATE_FLAG = true;
            this.angle += toRotate;
            this.internalRotate((Graphics2D)this.canvas.getGraphics());
        }
    }

    public void setRotation(double newAngle) {
        this.ROTATE_FLAG = true;
        this.angle = newAngle;
        this.internalRotate((Graphics2D)this.canvas.getGraphics());
    }

    public double getRotation() {
        return this.angle;
    }

    public void removeFromCanvas() {
        this.setCanvas(null);
    }

    private int getDrawOrder() {
        return this.canvas == null ? -1 : canvases.get(this.canvas).indexOf(this);
    }

    private void setDrawOrder(int pos) {
        if (this.canvas != null) {
            ArrayList<DrawableInterface> list = canvases.get(this.canvas);
            if (pos != -2 && pos < list.size()) {
                if (pos < 0) {
                    pos = 0;
                }
            } else {
                pos = list.size() - 1;
            }

            list.remove(this);
            list.add(pos, this);
            this.update();
        }
    }

    public void sendBackward() {
        this.setDrawOrder(this.getDrawOrder() - 1);
    }

    public void sendForward() {
        this.setDrawOrder(this.getDrawOrder() + 1);
    }

    public void sendToBack() {
        this.setDrawOrder(0);
    }

    public void sendToFront() {
        this.setDrawOrder(-2);
    }

    public void setColor(Color c) {
        this.color = c;
        this.update();
    }

    public void setColor(Color c, float a) {
        this.color = new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        this.update();
    }

    public void show() {
        this.hidden = false;
        this.update();
    }

    private String nameColor() {
        Color c = this.getColor();

        try {
            Field[] fields = Color.class.getDeclaredFields();

            for (Field f : fields) {
                char start = f.getName().charAt(0);
                if (start >= 'A' && start <= 'Z' && c.equals(f.get(null))) {
                    return "Color." + f.getName();
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ignored) {
        }

        return "new Color(" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + (c.getAlpha() == 255 ? "" : ", " + this.getColor().getAlpha()) + ")";
    }

    /** @deprecated */
    protected String toString(String middle) {
        return super.toString(middle + ", " + this.nameColor() + ", " + this.getCanvas());
    }

    static {
        DEFAULT_COLOR = Color.BLACK;
        canvases = new HashMap<>();
    }
}
