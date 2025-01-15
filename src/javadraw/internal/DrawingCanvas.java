//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;

public interface DrawingCanvas extends Dependent {
    /** @deprecated */
    Graphics getGraphics();

    /** @deprecated */
    boolean prepareImage(Image var1, ImageObserver var2);

    /** @deprecated */
    Image createImage(int var1, int var2);

    /** @deprecated */
    boolean requestFocusInWindow();

    /** @deprecated */
    void requestFocus();

    /** @deprecated */
    Rectangle getBounds();

    void clear();

    void repaint();

    void disableAutoRepaint();

    void enableAutoRepaint();

    Dimension getSize();

    int getWidth();

    int getHeight();

    void addMouseListener(MouseListener var1);

    void addMouseMotionListener(MouseMotionListener var1);

    void addComponentListener(ComponentListener var1);

    void addKeyListener(KeyListener var1);

    DrawableIterator getDrawableIterator();

    Color getBackground();

    void setBackground(Color var1);

    void setLoupe(Location var1, double var2, double var4);

    void setLoupe(Location var1);

    void clearLoupe();

    static SpawnPacket fromObject(Resizable2DInterface object) {
        SpawnPacket packet = new SpawnPacket();
        Location loc = new Location(object.getLocation().getDoubleX(), object.getLocation().getDoubleY());
        packet.setLocation(loc);
        packet.setWidth(object.getDoubleWidth());
        packet.setHeight(object.getDoubleHeight());
        packet.setRotation(object.getRotation());
        packet.setColor(object.getColor());
        if (object.getClass().getName().contains("Framed")) {
            packet.setFilled(false);
        } else {
            packet.setFilled(true);
        }

        if (object instanceof ObjectDrawRectangularShape) {
            packet.setType(ObjectType.RECT);
            if (object instanceof Rounded) {
                Rounded obj = (Rounded)object;
                packet.setArcWidth(obj.getDoubleArcWidth());
                packet.setArcHeight(obj.getDoubleArcHeight());
            }
        } else if (object instanceof Polygon) {
            packet.setType(ObjectType.POLY);
            Polygon poly = (Polygon)object;
            packet.setNumSides(poly.getNumSides());
        } else if (object instanceof ObjectDrawShape) {
            packet.setType(ObjectType.OVAL);
        }

        return packet;
    }

    static Resizable2DInterface getObject(SpawnPacket packet, DrawingCanvas canvas) throws Exception {
        ObjectType type = packet.getType();
        Resizable2DInterface object = null;
        switch (type) {
            case TEXT:
                Resizable2DInterface var5 = null;
                throw new Exception("Text object serialization is not implemented yet.");
            case RECT:
                if (packet.isFilled()) {
                    object = new FilledRect(packet.getLocation(), packet.getWidth(), packet.getHeight(), packet.getRotation(), packet.getColor(), canvas);
                } else {
                    object = new FramedRect(packet.getLocation(), packet.getWidth(), packet.getHeight(), packet.getRotation(), packet.getColor(), canvas);
                }
                break;
            case OVAL:
                if (packet.isFilled()) {
                    object = new FilledOval(packet.getLocation(), packet.getWidth(), packet.getHeight(), packet.getRotation(), packet.getColor(), canvas);
                } else {
                    object = new FramedOval(packet.getLocation(), packet.getWidth(), packet.getHeight(), packet.getRotation(), packet.getColor(), canvas);
                }
                break;
            case POLY:
                if (packet.isFilled()) {
                    object = new FilledPolygon(packet.getNumSides(), packet.getLocation(), packet.getWidth(), packet.getHeight(), packet.getRotation(), packet.getColor(), canvas);
                } else {
                    object = new FramedPolygon(packet.getNumSides(), packet.getLocation(), packet.getWidth(), packet.getHeight(), packet.getRotation(), packet.getColor(), canvas);
                }
                break;
            default:
                object = new FilledRect(packet.getLocation(), packet.getWidth(), packet.getHeight(), packet.getRotation(), packet.getColor(), canvas);
        }

        return object;
    }
}
