//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.util.HashMap;

public class VisibleImage extends ObjectDrawRectangularShape {
    private Image image;
    private static HashMap observers = new HashMap();
    private boolean manualSize;

    public VisibleImage(Image image, double x, double y, double width, double height, DrawingCanvas canvas) {
        super(x, y, width, height, true, (Color)null, canvas);
        this.manualSize = true;
        this.image = image;
        this.ready();
    }

    public VisibleImage(Image image, double x, double y, DrawingCanvas canvas) {
        this(image, x, y, (double)blockAndGetImageWidth(image), (double)blockAndGetImageHeight(image), canvas);
        this.manualSize = false;
    }

    public VisibleImage(Image image, Location point, double width, double height, DrawingCanvas canvas) {
        this(image, point.getDoubleX(), point.getDoubleY(), width, height, canvas);
    }

    public VisibleImage(Image image, Location point, DrawingCanvas canvas) {
        this(image, point.getDoubleX(), point.getDoubleY(), canvas);
        this.manualSize = false;
    }

    public VisibleImage(Image image, Location corner1, Location corner2, DrawingCanvas canvas) {
        this(image, corner1, corner2.getDoubleX() - corner1.getDoubleX(), corner2.getDoubleY() - corner1.getDoubleY(), canvas);
    }

    public int getImageWidth() {
        return blockAndGetImageWidth(this.image);
    }

    public int getImageHeight() {
        return blockAndGetImageHeight(this.image);
    }

    public static BufferedImage createBufferedCopy(Image image) {
        int width = blockAndGetImageWidth(image);
        int height = blockAndGetImageHeight(image);
        BufferedImage im = new BufferedImage(width, height, 2);
        im.getGraphics().drawImage(image, 0, 0, (ImageObserver)null);
        return im;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
        if (!this.manualSize) {
            super.setSize((double)blockAndGetImageWidth(image), (double)blockAndGetImageHeight(image));
        }

        this.update();
    }

    /** @deprecated */
    public void draw(Graphics2D g) {
        if (this.image != null) {
            blockAndLoadImage(this.image);
            g.drawImage(this.image, this.getX(), this.getY(), this.getWidth(), this.getHeight(), (ImageObserver)null);
        }

    }

    /** @deprecated */
    public Shape makeShape() {
        return new Rectangle2D.Double(this.getLocation().getDoubleX(), this.getLocation().getDoubleY(), this.getDoubleWidth(), this.getDoubleHeight());
    }

    private static int blockAndGetImageWidth(Image image) {
        if (image == null) {
            return 0;
        } else {
            blockAndLoadImage(image);
            return image.getWidth((ImageObserver)null);
        }
    }

    private static int blockAndGetImageHeight(Image image) {
        if (image == null) {
            return 0;
        } else {
            blockAndLoadImage(image);
            return image.getHeight((ImageObserver)null);
        }
    }

    private static BlockingImageObserver getObserver(Image image) {
        if (image == null) {
            return null;
        } else {
            BlockingImageObserver observer = (BlockingImageObserver)observers.get(image);
            if (observer == null) {
                observer = new BlockingImageObserver();
                observers.put(image, observer);
            }

            return observer;
        }
    }

    private static void blockAndLoadImage(Image image) {
        if (image != null && !(image instanceof RenderedImage)) {
            BlockingImageObserver observer = getObserver(image);
            if (image.getWidth(observer) <= 0) {
                while(!observer.isDone()) {
                    try {
                        synchronized(observer) {
                            observer.wait();
                        }
                    } catch (InterruptedException var5) {
                    }
                }

            }
        }
    }

    public void setSize(double width, double height) {
        super.setSize(width, height);
        this.manualSize = true;
    }

    public void setWidth(double w) {
        super.setWidth(w);
    }

    public void setHeight(double h) {
        super.setHeight(h);
    }

    public int getX() {
        return super.getX();
    }

    public int getY() {
        return super.getY();
    }

    public double getDoubleX() {
        return super.getDoubleX();
    }

    public double getDoubleY() {
        return super.getDoubleY();
    }

    public Location getLocation() {
        return super.getLocation();
    }

    public int getWidth() {
        return super.getWidth();
    }

    public int getHeight() {
        return super.getHeight();
    }

    public double getDoubleWidth() {
        return super.getDoubleWidth();
    }

    public double getDoubleHeight() {
        return super.getDoubleHeight();
    }

    public boolean overlaps(Drawable2DInterface other) {
        return super.overlaps(other);
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
        super.setColor(c);
    }

    public void setColor(Color c, float a) {
        super.setColor(c, a);
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
        return super.contains(point);
    }

    public String toString() {
        String str = "new VisibleImage(" + this.image + ", " + Text.formatDecimal(this.getDoubleX()) + ", " + Text.formatDecimal(this.getDoubleY()) + ", ";
        if (this.getWidth() != this.getImageWidth() || this.getHeight() != this.getImageHeight()) {
            str = str + Text.formatDecimal(this.getDoubleWidth()) + ", " + Text.formatDecimal(this.getDoubleHeight()) + ", ";
        }

        str = str + this.getCanvas() + ")";
        return str;
    }

    private static class BlockingImageObserver implements ImageObserver {
        private boolean done;

        private BlockingImageObserver() {
            this.done = false;
        }

        public boolean isDone() {
            return this.done;
        }

        public synchronized boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            if ((infoflags | 32) != 32 && (infoflags | 64) != 64) {
                return true;
            } else {
                this.done = true;
                this.notifyAll();
                return false;
            }
        }
    }
}
