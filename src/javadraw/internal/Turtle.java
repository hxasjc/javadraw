//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.HashMap;

public class Turtle extends ObjectDrawShape implements DrawableStrokeInterface, LocatableInterface {
    private BasicStroke stroke;
    private boolean pen;
    private TurtleFill fill;
    private TurtleLine line;
    private ArrayList<TurtleFill> fills;
    private ArrayList<TurtleLine> lines;
    private double heading;
    private boolean turtleHidden;
    private Location internalLocation;
    private AffineTransform sketchTransform;
    private double sketchScale;
    private double sketchRotation;
    private Location sketchOrigin;
    private final HashMap<Color, Color> recolorings;
    private static final int STEP_SIZE = 40;
    private static final Shape DEFAULT_SHAPE = defaultShape();
    private static final double[] t = new double[4];

    public void hideTurtle() {
        this.turtleHidden = true;
        this.update();
    }

    public void showTurtle() {
        this.turtleHidden = false;
        this.update();
    }

    public boolean isTurtleHidden() {
        return this.turtleHidden;
    }

    public void close() {
        this.line.close();
        this.line = null;
    }

    public void penUp() {
        this.pen = false;
        this.line = null;
        this.update();
    }

    public void penDown() {
        this.pen = true;
        this.update();
    }

    public boolean isPenDown() {
        return this.pen;
    }

    public double getHeading() {
        return this.heading + this.sketchRotation;
    }

    public void setHeading(double heading) {
        this.heading = (heading - this.sketchRotation) % (double)360.0F;
        this.update();
    }

    public void rt() {
        this.rt((double)90.0F);
    }

    public void rt(double angle) {
        this.setHeading(this.getHeading() - angle);
    }

    public void lt() {
        this.lt((double)90.0F);
    }

    public void lt(double angle) {
        this.setHeading(this.getHeading() + angle);
    }

    public void move() {
        this.move((double)40.0F);
    }

    public void move(double distance) {
        double ang = -this.heading * Math.PI / (double)180.0F;
        this.moveInternal(distance * Math.cos(ang), distance * Math.sin(ang));
    }

    public Turtle(double x, double y, Color color, DrawingCanvas canvas) {
        super((Location)null, false, color, canvas);
        this.internalLocation = new Location((double)0.0F, (double)0.0F);
        this.setMyLocation(new TurtleLocation(x, y));
        this.pen = true;
        this.recolorings = new HashMap<>();
        this.heading = (double)0.0F;
        this.stroke = DEFAULT_STROKE;
        this.turtleHidden = false;
        this.sketchOrigin = new TurtleLocation(x, y);
        this.sketchScale = (double)1.0F;
        this.sketchRotation = (double)0.0F;
        this.sketchTransform = new AffineTransform();
        this.clearSketch();
        this.rebuildTransform();
        this.ready();
    }

    public Turtle(Location start, Color color, DrawingCanvas canvas) {
        this(start.getDoubleX(), start.getDoubleY(), color, canvas);
    }

    public Turtle(double x, double y, DrawingCanvas canvas) {
        this(x, y, (Color)null, canvas);
    }

    public Turtle(Location start, DrawingCanvas canvas) {
        this(start, (Color)null, canvas);
    }

    public Turtle(Color color, DrawingCanvas canvas) {
        this((double)(canvas.getWidth() / 2), (double)(canvas.getHeight() / 2), color, canvas);
    }

    public Turtle(DrawingCanvas canvas) {
        this((Color)null, canvas);
    }

    public Turtle(Turtle clone, DrawingCanvas canvas) {
        super((Location)null, false, clone.getColor(), canvas);
        this.internalLocation = clone.internalLocation;
        this.setMyLocation(new TurtleLocation(clone.getDoubleX(), clone.getDoubleY()));
        this.pen = clone.pen;
        ArrayList<TurtleFill> activeFills = new ArrayList<>();
        this.fill = new TurtleFill(clone.fill);
        this.fills = new ArrayList<>();

        for(int i = 0; i < clone.fills.size() - activeFills.size(); ++i) {
            new TurtleFill((TurtleFill)clone.fills.get(i));
        }

        this.fills.addAll(activeFills);
        this.lines = new ArrayList<>();

        for(int i = 0; i < clone.lines.size(); ++i) {
            new TurtleLine((TurtleLine)clone.lines.get(i));
        }

        if (clone.line != null) {
            this.line = (TurtleLine)this.lines.get(this.lines.size() - 1);
        }

        this.recolorings = new HashMap<>(clone.recolorings);
        this.heading = clone.heading;
        this.stroke = clone.stroke;
        this.turtleHidden = clone.turtleHidden;
        this.sketchOrigin = new TurtleLocation(clone.getSketchDoubleX(), clone.getSketchDoubleY());
        this.sketchScale = clone.sketchScale;
        this.sketchRotation = clone.sketchRotation;
        this.sketchTransform = clone.sketchTransform;
        this.update();
    }

    private void moveInternal(double dx, double dy) {
        if (this.line == null) {
            this.line = new TurtleLine();
        }

        this.internalLocation.translate(dx, dy);
        this.line.move();
        if (this.fill != null) {
            this.fill.moved();
        }

        this.update();
    }

    private void translateColor(Graphics2D g, Color c) {
        Color translation = (Color)this.recolorings.get(c);
        if (translation == null) {
            g.setColor(c);
        } else {
            g.setColor(translation);
        }

    }

    /** @deprecated */
    public void draw(Graphics2D g) {
        AffineTransform saveAT = g.getTransform();
        g.transform(this.sketchTransform);
        if (this.lines != null) {
            for (TurtleFill turtleFill : this.fills) {
                TurtleDrawable draw = (TurtleDrawable) turtleFill;
                draw.draw(g);
            }

            for(int i = 0; i < this.lines.size(); ++i) {
                TurtleDrawable draw = (TurtleDrawable)this.lines.get(i);
                draw.draw(g);
            }

            if (!this.turtleHidden) {
                Shape shape = this.makeTurtleShape();
                g.setStroke(this.stroke);
                this.translateColor(g, this.getColor());
                if (this.pen) {
                    g.fill(shape);
                }

                g.draw(shape);
            }

            g.setTransform(saveAT);
        }
    }

    private Shape makeTurtleShape() {
        double ang = this.heading * Math.PI / (double)180.0F;
        double cos = Math.cos(ang);
        double sin = Math.sin(ang);
        AffineTransform n = new AffineTransform(cos, -sin, sin, cos, this.internalLocation.getDoubleX(), this.internalLocation.getDoubleY());
        return n.createTransformedShape(DEFAULT_SHAPE);
    }

    /** @deprecated */
    public static Shape defaultShape() {
        GeneralPath me = new GeneralPath();
        me.moveTo(0.0F, 0.0F);
        me.lineTo(-4.0F, -8.0F);
        me.lineTo(12.0F, 0.0F);
        me.lineTo(-4.0F, 8.0F);
        me.closePath();
        return me;
    }

    public void beginFill(Color fillColor) {
        new TurtleFill(fillColor);
    }

    public void endFill() {
        if (this.fill != null) {
            this.fill.pop();
        }

    }

    /** @deprecated */
    public Shape makeShape() {
        Area sketchShape = new Area();

        for(int i = 0; i < this.fills.size(); ++i) {
            TurtleDrawable draw = (TurtleDrawable)this.fills.get(i);
            draw.addShape(sketchShape);
        }

        for(int i = 0; i < this.lines.size(); ++i) {
            TurtleDrawable draw = (TurtleDrawable)this.lines.get(i);
            draw.addShape(sketchShape);
        }

        return this.sketchTransform.createTransformedShape(sketchShape);
    }

    public void clearSketch() {
        this.fills = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.fill = null;
        this.line = null;
        this.update();
    }

    public void setSketchRotation(double angle) {
        this.sketchRotation = angle % (double)360.0F;
        this.rebuildTransform();
    }

    public void rotateSketch(double byAngle) {
        this.setSketchRotation(this.sketchRotation + byAngle);
    }

    public double getSketchRotation() {
        return this.sketchRotation;
    }

    public void moveSketch(double dx, double dy) {
        this.sketchOrigin.translate(dx, dy);
    }

    public void moveSketchTo(double x, double y) {
        this.moveSketch(x - this.sketchOrigin.getDoubleX(), y - this.sketchOrigin.getDoubleY());
    }

    public void moveSketchTo(Location point) {
        this.moveSketchTo(point.getDoubleX(), point.getDoubleY());
    }

    public int getSketchX() {
        return (int)(this.sketchOrigin.getDoubleX() + (double)0.5F);
    }

    public int getSketchY() {
        return (int)(this.sketchOrigin.getDoubleY() + (double)0.5F);
    }

    public double getSketchDoubleX() {
        return this.sketchOrigin.getDoubleX();
    }

    public double getSketchDoubleY() {
        return this.sketchOrigin.getDoubleY();
    }

    public Location getSketchLocation() {
        return this.sketchOrigin;
    }

    public void setSketchScale(double factor) {
        this.sketchScale = factor;
        this.rebuildTransform();
    }

    public void scaleSketch(double byFactor) {
        this.setSketchScale(this.sketchScale * byFactor);
    }

    public double getSketchScale() {
        return this.sketchScale;
    }

    public void recolorSketch(Color original, Color substitute) {
        this.recolorings.put(original, substitute);
    }

    private void rebuildTransform() {
        this.sketchTransform.setToIdentity();
        this.sketchTransform.translate(this.sketchOrigin.getDoubleX(), this.sketchOrigin.getDoubleY());
        this.sketchTransform.rotate(-this.sketchRotation * Math.PI / (double)180.0F);
        this.sketchTransform.scale(this.sketchScale, this.sketchScale);
        this.update();
    }

    public double getLineWidth() {
        return (double)this.stroke.getLineWidth();
    }

    public BasicStroke getStroke() {
        return this.stroke;
    }

    public void setLineWidth(double width) {
        this.stroke = new BasicStroke((float)width, this.stroke.getEndCap(), this.stroke.getLineJoin(), this.stroke.getMiterLimit(), this.stroke.getDashArray(), this.stroke.getDashPhase());
        this.line = null;
        this.update();
    }

    public void setStroke(BasicStroke stroke) {
        this.stroke = stroke;
        this.line = null;
        this.update();
    }

    public int getX() {
        return this.getLocation().getX();
    }

    public int getY() {
        return this.getLocation().getY();
    }

    public double getDoubleX() {
        return this.getLocation().getDoubleX();
    }

    public double getDoubleY() {
        return this.getLocation().getDoubleY();
    }

    public Location getLocation() {
        return this.getMyLocation();
    }

    public void hide() {
        super.hide();
    }

    public void show() {
        super.show();
    }

    public boolean isHidden() {
        return super.isHidden();
    }

    public void addToCanvas(DrawingCanvas c) {
        super.addToCanvas(c);
    }

    public void removeFromCanvas() {
        super.removeFromCanvas();
    }

    public DrawingCanvas getCanvas() {
        return super.getCanvas();
    }

    public void moveTo(Location point) {
        super.moveTo(point);
    }

    public void moveTo(double x, double y) {
        super.moveTo(x, y);
    }

    public void move(double dx, double dy) {
        super.move(dx, dy);
    }

    public Color getColor() {
        return super.getColor();
    }

    public void setColor(Color c) {
        this.line = null;
        super.setColor(c);
    }

    public void sendForward() {
        super.sendForward();
    }

    public void sendBackward() {
        super.sendBackward();
    }

    public void sendToFront() {
        super.sendToFront();
    }

    public void sendToBack() {
        super.sendToBack();
    }

    public boolean contains(Location point) {
        return this.getShape().contains(point.getDoubleX(), point.getDoubleY());
    }

    public String toString() {
        return this.toString(Text.formatDecimal(this.getDoubleX()) + ", " + Text.formatDecimal(this.getDoubleY()));
    }

    private abstract static class TurtleDrawable {
        private TurtleDrawable() {
        }

        public abstract void addShape(Area var1);

        public abstract void draw(Graphics2D var1);
    }

    private class TurtleLine extends TurtleDrawable {
        private Color color;
        private GeneralPath path;
        private BasicStroke stroke;

        public TurtleLine(TurtleLine clone) {
            this.color = clone.color;
            this.stroke = clone.stroke;
            Turtle.this.lines.add(this);
            this.path = new GeneralPath(clone.path);
        }

        public TurtleLine() {
            this.color = Turtle.this.getColor();
            this.stroke = Turtle.this.stroke;
            Turtle.this.lines.add(this);
            this.path = new GeneralPath();
            this.path.moveTo((float)Turtle.this.internalLocation.getDoubleX(), (float)Turtle.this.internalLocation.getDoubleY());
        }

        public void move() {
            if (Turtle.this.pen) {
                this.path.lineTo((float)Turtle.this.internalLocation.getDoubleX(), (float)Turtle.this.internalLocation.getDoubleY());
            } else {
                this.path.moveTo((float)Turtle.this.internalLocation.getDoubleX(), (float)Turtle.this.internalLocation.getDoubleY());
            }

        }

        public void draw(Graphics2D g) {
            Turtle.this.translateColor(g, this.color);
            g.setStroke(this.stroke);
            g.draw(this.path);
        }

        public void addShape(Area shape) {
            shape.add(new Area(this.stroke.createStrokedShape(this.path)));
        }

        public void close() {
            this.path.closePath();
        }
    }

    private class TurtleFill extends TurtleDrawable {
        private GeneralPath path;
        private TurtleFill next;
        private boolean outline;
        private Color fillColor;

        public TurtleFill(TurtleFill clone) {
            this.path = new GeneralPath(clone.path);
            this.fillColor = clone.fillColor;
            if (clone.next != null) {
                this.next = Turtle.this.new TurtleFill(clone.next);
            }

            Turtle.this.fills.add(this);
        }

        public TurtleFill(Color fillColor) {
            this.path = new GeneralPath();
            this.path.moveTo((float)Turtle.this.internalLocation.getDoubleX(), (float)Turtle.this.internalLocation.getDoubleY());
            this.next = Turtle.this.fill;
            Turtle.this.fill = this;
            this.fillColor = fillColor;
            Turtle.this.fills.add(this);
        }

        public void pop() {
            Turtle.this.fill = this.next;
        }

        public void moved() {
            if (Turtle.this.pen) {
                this.path.lineTo((float)Turtle.this.internalLocation.getDoubleX(), (float)Turtle.this.internalLocation.getDoubleY());
            } else {
                this.path.closePath();
                this.path.moveTo((float)Turtle.this.internalLocation.getDoubleX(), (float)Turtle.this.internalLocation.getDoubleY());
            }

        }

        public void draw(Graphics2D g) {
            Turtle.this.translateColor(g, this.fillColor);
            g.fill(this.path);
        }

        public boolean contains(Location point) {
            return this.path.contains(point.getDoubleX(), point.getDoubleY());
        }

        public void addShape(Area shape) {
            shape.add(new Area(this.path));
        }
    }

    private class TurtleLocation extends Location {
        public TurtleLocation(double x, double y) {
            super(x, y);
        }

        public void translate(double dx, double dy) {
            super.translate(dx, dy);
            if (this == Turtle.this.sketchOrigin) {
                Turtle.this.rebuildTransform();
            } else {
                synchronized(Turtle.t) {
                    Turtle.t[0] = dx;
                    Turtle.t[1] = dy;

                    try {
                        Turtle.this.sketchTransform.createInverse().transform(Turtle.t, 0, Turtle.t, 2, 1);
                        Turtle.this.moveInternal(Turtle.t[2] + Turtle.this.sketchTransform.getTranslateX(), Turtle.t[3] + Turtle.this.sketchTransform.getTranslateY());
                    } catch (NoninvertibleTransformException ignored) {
                    }
                }
            }

        }
    }
}
