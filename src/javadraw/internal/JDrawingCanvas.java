//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import lombok.SneakyThrows;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * @deprecated
 */
public class JDrawingCanvas extends JComponent implements DrawingCanvas {
    private static final Color MINOR_GRID_COLOR = new Color(0, 0, 0, 40);
    private static final Color MAJOR_GRID_COLOR = new Color(0, 0, 0, 80);
    private boolean auto;
    private Location loupe = null;
    private double loupeSize = 30.0F;
    private double loupeZoom = 3.0F;
    private static int ID = 0;
    private final int id;
    private BufferedImage buffer;
    private boolean needsRepainting = false;
    private final WindowController controller;

    public JDrawingCanvas(WindowController controller) {
        this.controller = controller;
        this.setOpaque(true);
        this.setBackground(Color.WHITE);
        this.setFocusTraversalKeysEnabled(false);
        this.setPreferredSize(new Dimension(400, 400));
        this.auto = true;
        this.id = ID++;
    }

    public void clear() {
        ObjectDrawShape.clearCanvas(this);
    }

    public void disableAutoRepaint() {
        this.auto = false;
    }

    public void enableAutoRepaint() {
        this.auto = true;
    }

    public void repaint() {
        this.needsRepainting = true;
        super.repaint();
    }

    public DrawableIterator getDrawableIterator() {
        return ObjectDrawShape.getDrawables(this);
    }

    public void update() {
        if (this.auto) {
            this.needsRepainting = true;
            this.repaint();
        }

    }

    private void createBuffer() {
        if (this.buffer == null || this.buffer.getWidth(null) != this.getWidth() || this.buffer.getHeight(null) != this.getHeight()) {
            this.buffer = new BufferedImage(this.getWidth(), this.getHeight(), 1);
        }
    }

    private void repaintBuffer() {
        if (this.needsRepainting) {
            this.createBuffer();
            Graphics2D g = this.buffer.createGraphics();
            ObjectDrawShape.draw(this, g);
            if (this.loupe != null) {
                Ellipse2D loupeArea = new Ellipse2D.Double(this.loupe.getDoubleX() - this.loupeZoom * this.loupeSize / (double) 2.0F, this.loupe.getDoubleY() - this.loupeZoom * this.loupeSize / (double) 2.0F, this.loupeZoom * this.loupeSize, this.loupeZoom * this.loupeSize);
                g.setColor(this.getBackground());
                g.fill(loupeArea);
                g.setStroke(new BasicStroke(4.0F));
                g.draw(loupeArea);
                g.setStroke(new BasicStroke(2.0F));
                g.setColor(new Color(16777215 - (16777215 & this.getBackground().getRGB())));
                g.draw(loupeArea);
                g.clip(loupeArea);
                AffineTransform t = AffineTransform.getTranslateInstance(this.loupe.getDoubleX(), this.loupe.getDoubleY());
                t.scale(this.loupeZoom, this.loupeZoom);
                t.translate(-this.loupe.getDoubleX(), -this.loupe.getDoubleY());
                g.transform(t);
                ObjectDrawShape.draw(this, g);
            }

            g.dispose();
            this.needsRepainting = false;
        }
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        this.repaintBuffer();
        graphics.drawImage(this.buffer, 0, 0, null);
        if (this.controller.isGridVisible()) {
            this.paintGrid(graphics);
        }

    }

    private void paintGrid(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        g.setColor(MINOR_GRID_COLOR);
        int i = 4;

        for (int x = 10; x < w; --i) {
            if (i == 0) {
                g.setColor(MAJOR_GRID_COLOR);
                g.drawLine(x, 0, x, h);
                g.setColor(MINOR_GRID_COLOR);
                i = 5;
            } else {
                g.drawLine(x, 0, x, h);
            }

            x += 10;
        }

        i = 4;

        for (int y = 10; y < h; --i) {
            if (i == 0) {
                g.setColor(MAJOR_GRID_COLOR);
                g.drawLine(0, y, w, y);
                g.setColor(MINOR_GRID_COLOR);
                i = 5;
            } else {
                g.drawLine(0, y, w, y);
            }

            y += 10;
        }

    }

    public void clearLoupe() {
        this.loupe = null;
        this.repaint();
    }

    public void setLoupe(Location point, double size, double zoom) {
        this.loupe = new Location(point);
        this.loupeSize = size;
        this.loupeZoom = zoom;
        this.repaint();
    }

    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        this.repaint();
    }

    public void setLoupe(Location point) {
        this.loupe = new Location(point);
        this.repaint();
    }

    @Override
    @SneakyThrows
    public void screenshot(File file) {
        screenshot(file, null);
    }

    @Override
    public void screenshot(File file, Consumer<Throwable> errorHandler) {
        try {
            Rectangle bounds = getBounds();
            BufferedImage bufferedImage = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
            paint(bufferedImage.getGraphics());

            File temp = File.createTempFile("screenshot", "png");

            ImageIO.write(bufferedImage, "png", file);

            temp.deleteOnExit();
        } catch (IOException e) {
            errorHandler.accept(e);
        }
    }

    public String toString() {
        return this.id == 0 ? "canvas" : "canvas" + this.id;
    }
}
