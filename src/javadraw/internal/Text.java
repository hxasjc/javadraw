//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Text extends ObjectDrawShape implements Drawable2DInterface, TextWrappingLayout.TextRun, Serializable {
    private DrawableInterface basis;
    private double alignH;
    private double alignV;
    private String text;
    private Font font;
    private Text next;
    public static final int TOP = -1;
    public static final int BOTTOM = 1;
    public static final int LEFT = -1;
    public static final int RIGHT = 1;
    public static final int CENTER = 0;
    public static final Font DEFAULT_FONT = Font.decode("Times-PLAIN-12");

    public static String formatDecimal(double num) {
        return formatDecimal(num, 4);
    }

    public static String formatDecimal(double num, int places) {
        String sign = num < (double)0.0F ? "-" : "";
        String tail = "";
        double min = 1.0F;

        for(int i = 0; i < places; ++i) {
            min /= 10.0F;
        }

        if (num < (double)0.0F) {
            num = -num;
        }

        if (num >= (double)1.0E9F) {
            int exponent;
            for(exponent = 0; num >= (double)10.0F; ++exponent) {
                num /= 10.0F;
            }

            tail = "e" + exponent + tail;
        } else {
            if (num <= 1.0E-10) {
                return "0";
            }

            if (num <= min) {
                int exponent;
                for(exponent = 0; num < (double)1.0F; ++exponent) {
                    num *= 10.0F;
                }

                tail = "e-" + exponent + tail;
            }
        }

        int ipart = (int)num;
        num -= ipart;
        int place = 1;
        double adjust = 0.5F;

        for(int i = 0; i < places; ++i) {
            place *= 10;
            adjust /= 10.0F;
        }

        int d;
        for(d = (int)((num + adjust) * (double)place) % 10; d == 0 && place > 1; d = (int)((num + adjust) * (double)place) % 10) {
            place /= 10;
            adjust *= 10.0F;
        }

        if (place <= 1) {
            return sign + ipart + tail;
        } else {
            while(place > 1) {
                tail = d + tail;
                place /= 10;
                d = (int)((num + adjust) * (double)place) % 10;
            }

            return sign + ipart + "." + tail;
        }
    }

    private Text(Object text, double x, double y, DrawableInterface basis, Color color, DrawingCanvas canvas) {
        super(new Location(x, y), true, color, canvas);
        this.alignH = -1.0F;
        this.alignV = 1.0F;
        this.font = DEFAULT_FONT;
        this.basis = basis;
        this.text = text + "";
        if (basis instanceof ObjectDrawShape) {
            ((ObjectDrawObject)basis).depend(this);
            if (basis instanceof Text) {
                Text b = (Text)basis;
                if (b.next != null) {
                    this.next = b.next;
                    this.next.basis = this;
                }

                b.next = this;
            }
        }

        this.update();
        this.ready();
    }

    public Text(Object text, double x, double y, Color color, DrawingCanvas canvas) {
        this(text, x, y, null, color, canvas);
    }

    private static ObjectDrawShape checkBasis(DrawableInterface basis) {
        if (!(basis instanceof ObjectDrawShape)) {
            String name = basis.getClass().getName();
            name = name.substring(name.indexOf(".") + 1);
            throw new IllegalArgumentException("Text is not able to lay itself out in a " + name + ".");
        } else {
            return (ObjectDrawShape)basis;
        }
    }

    public Text(Object text, DrawableInterface basis, Color color, DrawingCanvas canvas) {
        this(text, checkBasis(basis).getMyLocation().getX(), checkBasis(basis).getMyLocation().getY(), basis, color, canvas);
    }

    public Text(Object text, Location origin, Color color, DrawingCanvas canvas) {
        this(text, origin.getDoubleX(), origin.getDoubleY(), color, canvas);
    }

    public Text(boolean text, double x, double y, Color color, DrawingCanvas canvas) {
        this("" + text, x, y, color, canvas);
    }

    public Text(boolean text, DrawableInterface basis, Color color, DrawingCanvas canvas) {
        this("" + text, basis, color, canvas);
    }

    public Text(boolean text, Location origin, Color color, DrawingCanvas canvas) {
        this("" + text, origin, color, canvas);
    }

    public Text(char text, double x, double y, Color color, DrawingCanvas canvas) {
        this("" + text, x, y, color, canvas);
    }

    public Text(char text, DrawableInterface basis, Color color, DrawingCanvas canvas) {
        this("" + text, basis, color, canvas);
    }

    public Text(char text, Location origin, Color color, DrawingCanvas canvas) {
        this("" + text, origin, color, canvas);
    }

    public Text(double text, double x, double y, Color color, DrawingCanvas canvas) {
        this(formatDecimal(text), x, y, color, canvas);
    }

    public Text(double text, DrawableInterface basis, Color color, DrawingCanvas canvas) {
        this(formatDecimal(text), basis, color, canvas);
    }

    public Text(double text, Location origin, Color color, DrawingCanvas canvas) {
        this(formatDecimal(text), origin, color, canvas);
    }

    public Text(long text, double x, double y, Color color, DrawingCanvas canvas) {
        this("" + text, x, y, color, canvas);
    }

    public Text(long text, DrawableInterface basis, Color color, DrawingCanvas canvas) {
        this("" + text, basis, color, canvas);
    }

    public Text(long text, Location origin, Color color, DrawingCanvas canvas) {
        this("" + text, origin, color, canvas);
    }

    public Text(Object text, double x, double y, DrawingCanvas canvas) {
        this(text, x, y, null, canvas);
    }

    public Text(Object text, DrawableInterface basis, DrawingCanvas canvas) {
        this(text, basis, null, canvas);
    }

    public Text(Object text, Location origin, DrawingCanvas canvas) {
        this(text, origin.getDoubleX(), origin.getDoubleY(), canvas);
    }

    public Text(boolean text, double x, double y, DrawingCanvas canvas) {
        this(text, x, y, null, canvas);
    }

    public Text(boolean text, DrawableInterface basis, DrawingCanvas canvas) {
        this(text, basis, null, canvas);
    }

    public Text(boolean text, Location origin, DrawingCanvas canvas) {
        this(text, origin, null, canvas);
    }

    public Text(char text, double x, double y, DrawingCanvas canvas) {
        this(text, x, y, null, canvas);
    }

    public Text(char text, DrawableInterface basis, DrawingCanvas canvas) {
        this(text, basis, null, canvas);
    }

    public Text(char text, Location origin, DrawingCanvas canvas) {
        this(text, origin, null, canvas);
    }

    public Text(double text, double x, double y, DrawingCanvas canvas) {
        this(text, x, y, null, canvas);
    }

    public Text(double text, DrawableInterface basis, DrawingCanvas canvas) {
        this(text, basis, null, canvas);
    }

    public Text(double text, Location origin, DrawingCanvas canvas) {
        this(text, origin, null, canvas);
    }

    public Text(long text, double x, double y, DrawingCanvas canvas) {
        this(text, x, y, null, canvas);
    }

    public Text(long text, DrawableInterface basis, DrawingCanvas canvas) {
        this(text, basis, null, canvas);
    }

    public Text(long text, Location origin, DrawingCanvas canvas) {
        this(text, origin, null, canvas);
    }

    private Shape setEmptyShapes() {
        Shape shape = new GeneralPath();
        this.setMyShape(shape);
        if (this.next != null) {
            this.next.setEmptyShapes();
        }

        return shape;
    }

    /** @deprecated */
    public Shape makeShape() {
        if (this.getCanvas() == null) {
            return null;
        } else {
            Graphics2D g2 = (Graphics2D)((JDrawingCanvas)this.getCanvas()).getGraphics();
            g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            if (this.basis instanceof Text) {
                ((Text)this.basis).makeShape();
            } else if (this.basis != null && !(this.basis instanceof Line)) {
                Shape basisShape = this.basis.getShape();
                (new TextWrappingLayout(this, this.alignH, this.alignV, basisShape, g2.getFontRenderContext())).doLayout();
            } else {
                (new TextWrappingLayout(this, this.alignH, this.alignV, null, g2.getFontRenderContext())).doLayout();
            }

            return this.getShape();
        }
    }

    public void setAlignment(double horizontalAlign, double verticalAlign) {
        if (this.basis instanceof Text) {
            ((Text)this.basis).setAlignment(horizontalAlign, verticalAlign);
        } else {
            this.alignH = horizontalAlign;
            this.alignV = verticalAlign;
            this.update();
        }

    }

    public double getHorizontalAlignment() {
        return this.basis instanceof Text ? ((Text)this.basis).getHorizontalAlignment() : this.alignH;
    }

    public double getVerticalAlignment() {
        return this.basis instanceof Text ? ((Text)this.basis).getVerticalAlignment() : this.alignV;
    }

    public Font getFont() {
        return this.font;
    }

    public void setFont(Font f) {
        this.font = f;
        this.update();
    }

    public String getText() {
        return this.text;
    }

    public void setText(String t) {
        this.text = t == null ? "" : t;
        this.update();
    }

    public void setFont(String fname) {
        this.setFont(new Font(fname, this.font.getStyle(), this.font.getSize()));
    }

    public void setPlain() {
        this.setFont(this.font.deriveFont(Font.PLAIN));
    }

    public void setBold(boolean b) {
        this.setFont(this.font.deriveFont(b ? this.font.getStyle() | 1 : this.font.getStyle() & -2));
    }

    public void setItalic(boolean i) {
        this.setFont(this.font.deriveFont(i ? this.font.getStyle() | 2 : this.font.getStyle() & -3));
    }

    public void setFontSize(int size) {
        this.setFont(this.font.deriveFont((float)size));
    }

    public void setBold() {
        this.setBold(true);
    }

    public void setItalic() {
        this.setItalic(true);
    }

    /** @deprecated */
    public Rectangle2D getBounds() {
        return this.getShape().getBounds();
    }

    public Text getNext() {
        return this.next;
    }

    /** @deprecated */
    public TextWrappingLayout.TextRun getNextRun() {
        return this.next;
    }

    private Line getBasisLine() {
        if (this.basis instanceof Line) {
            return (Line)this.basis;
        } else {
            return this.basis instanceof Text ? ((Text)this.basis).getBasisLine() : null;
        }
    }

    private Location getBasisLocation() {
        if (this.basis == null) {
            return this.getMyLocation();
        } else {
            return this.basis instanceof Text ? ((Text)this.basis).getBasisLocation() : null;
        }
    }

    /** @deprecated */
    public void setShape(Shape shape) {
        Line line = this.getBasisLine();
        if (line != null) {
            double x = line.getStart().getDoubleX();
            double y = line.getStart().getDoubleY();
            double dx = line.getEnd().getDoubleX() - x;
            double dy = line.getEnd().getDoubleY() - y;
            double length = line.getStart().distanceTo(line.getEnd());
            AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
            transform.rotate(Math.atan2(dy, dx));
            transform.translate(length * (this.getHorizontalAlignment() + (double)1.0F) / (double)2.0F, 0.0F);
            shape = transform.createTransformedShape(shape);
        } else {
            Location loc;
            if ((loc = this.getBasisLocation()) != null) {
                AffineTransform transform = AffineTransform.getTranslateInstance(loc.getDoubleX(), loc.getDoubleY());
                shape = transform.createTransformedShape(shape);
            }
        }

        this.setMyShape(shape);
    }

    private DrawingCanvas getBasisCanvas() {
        if (this.basis == null) {
            return this.getCanvas();
        } else {
            return this.basis instanceof Text ? ((Text)this.basis).getBasisCanvas() : this.basis.getCanvas();
        }
    }

    /** @deprecated */
    public void update() {
        DrawingCanvas c = this.getBasisCanvas();
        if (c != this.getCanvas()) {
            if (c == null) {
                this.removeFromCanvas();
            } else {
                this.addToCanvas(c);
            }
        }

        if (this.next != null) {
            this.updateForward();
        }

        this.updateBackward();
    }

    private void updateForward() {
        if (this.next != null) {
            this.next.updateForward();
        }

        super.update();
    }

    private void updateBackward() {
        if (this.basis instanceof Text) {
            ((Text)this.basis).updateBackward();
        }

        super.update();
    }

    public Text getPrevious() {
        return this.basis instanceof Text ? (Text)this.basis : null;
    }

    public DrawableInterface getBasis() {
        return this.basis instanceof Text ? ((Text)this.basis).getBasis() : this.basis;
    }

    private String escapeText() {
        String s = "";

        for(int i = 0; i < this.text.length(); ++i) {
            char c = this.text.charAt(i);
            if (c == '\\') {
                s = s + "\\\\";
            } else if (c == '"') {
                s = s + "\\\"";
            } else if (c == '\n') {
                s = s + "\\n";
            } else if (c == '\t') {
                s = s + "\\t";
            } else if (c == '\r') {
                s = s + "\\r";
            } else {
                s = s + c;
            }
        }

        return "\"" + s + "\"";
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
        if (this.basis == null) {
            return this.getMyLocation();
        } else {
            return this.basis instanceof Text ? ((Text)this.basis).getLocation() : ((ObjectDrawShape)this.basis).getMyLocation();
        }
    }

    public int getWidth() {
        return (int)(this.getBounds().getWidth() + (double)0.5F);
    }

    public int getHeight() {
        return (int)(this.getBounds().getHeight() + (double)0.5F);
    }

    public double getDoubleWidth() {
        return this.getBounds().getWidth();
    }

    public double getDoubleHeight() {
        return this.getBounds().getHeight();
    }

    public boolean overlaps(Drawable2DInterface other) {
        return this.getBounds().intersects(other.getBounds());
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
        if (this.basis instanceof Text) {
            ((Text)this.basis).next = this.next;
        }

        if (this.next != null) {
            this.next.alignH = this.alignH;
            this.next.alignV = this.alignV;
            this.next.basis = this.basis;
            this.next.update();
        } else if (this.basis instanceof Text) {
            ((Text)this.basis).update();
        }

        this.basis = null;
        this.next = null;
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
        if (this.basis == null) {
            this.getMyLocation().translate(dx, dy);
        } else {
            this.basis.move(dx, dy);
        }

    }

    public Color getColor() {
        return super.getColor();
    }

    public void setColor(Color c) {
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
        return this.getShape() == null ? false : this.getShape().getBounds2D().contains(point.getX(), point.getY());
    }

    public String toString() {
        return this.toString(this.escapeText() + ", " + this.basis);
    }
}
