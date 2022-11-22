//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import javadraw.KeyEventListener;
import javadraw.MouseEventListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JComponent;
import javax.swing.border.AbstractBorder;

public class WindowController extends Controller {
    protected final DrawingCanvas canvas = new JDrawingCanvas(this);
    private boolean grid = false;
    private boolean rulers = false;
    private final RulerBorder rulerBorder;
    private static final Color RULER_COLOR = new Color(50, 50, 50);
    private static final Color RULER_BACKGROUND = new Color(200, 200, 200);
    private static final Font RULER_FONT = Font.decode("Times-BOLD-12");
    private static final Color TICK_COLOR;
    private final KeyInterpreter key;
    private final MouseInterpreter mouse;

    void setControllerSize(Dimension d) {
        ((JDrawingCanvas)this.canvas).setPreferredSize(d);
    }

    public WindowController() {
        this.getContentPane().add((JDrawingCanvas)this.canvas, "Center");
        this.grid = this.rulers = false;
        this.rulerBorder = new RulerBorder();
        this.key = new KeyInterpreter(this, this.canvas);
        this.mouse = new MouseInterpreter(this, this.canvas);
    }

    public void setGridVisible(boolean visible) {
        if (this.grid != visible) {
            this.grid = visible;
            this.canvas.repaint();
        }
    }

    public boolean isGridVisible() {
        return this.grid;
    }

    public void setRulersVisible(boolean visible) {
        if (this.rulers != visible) {
            this.rulers = visible;
            ((JComponent)this.getContentPane()).setBorder(visible ? this.rulerBorder : null);
            this.rulerBorder.setAttached(visible);
        }
    }

    public boolean areRulersVisible() {
        return this.rulers;
    }

    public DrawingCanvas getCanvas() {
        return this.canvas;
    }

    /** @deprecated */
    @Deprecated
    public void start() {
        this.canvas.clear();

        while(this.canvas.getWidth() <= 0 || this.canvas.getHeight() <= 0) {
            try {
                Thread.sleep(20L);
            } catch (InterruptedException var2) {
            }
        }

        super.start();
    }

    /** @deprecated */
    @Deprecated
    public void stop() {
        super.stop();
        this.canvas.clear();
    }

    public void begin() {
    }

    public void onMouseClick(int button, Location point) {
    }

    public void onMouseDrag(int button, Location point) {
    }

    public void onMouseMove(int button, Location point) {
    }

    public void onMouseEnter(int button, Location point) {
    }

    public void onMouseExit(int button, Location point) {
    }

    public void onMousePress(int button, Location point) {
    }

    public void onMouseRelease(int button, Location point) {
    }

    public void onKeyPress(String keyChar) {
    }

    public void onKeyRelease(String keyChar) {
    }

    static {
        TICK_COLOR = Color.BLUE;
    }

    private class RulerBorder extends AbstractBorder implements MouseListener, MouseMotionListener {
        private int x;
        private int y;

        private RulerBorder() {
            this.x = -1;
            this.y = -1;
        }

        public void setAttached(boolean attached) {
            if (attached) {
                ((JDrawingCanvas)WindowController.this.canvas).addMouseMotionListener(this);
                ((JDrawingCanvas)WindowController.this.canvas).addMouseListener(this);
            } else {
                ((JDrawingCanvas)WindowController.this.canvas).removeMouseMotionListener(this);
                ((JDrawingCanvas)WindowController.this.canvas).removeMouseListener(this);
            }

        }

        public void paintBorder(Component c, Graphics graphics, int bx, int by, int width, int height) {
            Graphics2D g = (Graphics2D)graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g.translate(bx, by);
            g.setColor(WindowController.RULER_BACKGROUND);
            g.fillRect(0, 0, width, 30);
            g.fillRect(0, 30, 30, height - 30);
            g.setColor(WindowController.RULER_COLOR);
            g.setFont(WindowController.RULER_FONT);
            FontMetrics fm = g.getFontMetrics();
            int d = 30;

            int i;
            for(i = 0; d < width + 30; --i) {
                if (i == 0) {
                    g.drawLine(d, 22, d, 30);
                    i = 5;
                } else {
                    g.drawLine(d, 25, d, 30);
                }

                d += 10;
            }

            g.drawLine(30, 29, width, 29);

            String sx;
            for(d = 50; d < width; d += 50) {
                sx = Integer.toString(d);
                g.drawString(sx, 30 + d - fm.stringWidth(sx) / 2, 20);
            }

            d = 30;

            for(i = 0; d < height + 30; --i) {
                if (i == 0) {
                    g.drawLine(22, d, 30, d);
                    i = 5;
                } else {
                    g.drawLine(25, d, 30, d);
                }

                d += 10;
            }

            g.drawLine(29, 30, 29, height);
            d = fm.getAscent() / 2;

            for(i = 50; i < height; i += 50) {
                String s = Integer.toString(i);
                g.drawString(s, 20 - fm.stringWidth(s), 30 + i + d);
            }

            if (this.x >= 0) {
                g.drawString("(" + this.x + ",", 2, fm.getAscent());
                sx = this.y + ")";
                g.drawString(sx, 28 - fm.stringWidth(sx), fm.getAscent() + fm.getHeight());
                g.setColor(WindowController.TICK_COLOR);
                g.drawLine(this.x + 30, 20, this.x + 30, 30);
                g.drawLine(20, this.y + 30, 30, this.y + 30);
                g.translate(-bx, -by);
            }
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(30, 30, 0, 0);
        }

        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = 30;
            insets.right = insets.bottom = 0;
            return insets;
        }

        public void mouseDragged(MouseEvent e) {
            this.x = e.getX();
            this.y = e.getY();
            WindowController.this.repaint();
        }

        public void mouseExited(MouseEvent e) {
            this.x = -1;
            WindowController.this.repaint();
        }

        public void mouseMoved(MouseEvent e) {
            this.mouseDragged(e);
        }

        public void mouseEntered(MouseEvent e) {
            this.mouseMoved(e);
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }

    public void addMouseEventListener(String type, MouseEventListener listener) {
        mouse.addListener(type, listener);
    }

    public void addKeyEventListener(String type, KeyEventListener listener) {
        key.addKeyEventListener(type, listener);
    }
}
