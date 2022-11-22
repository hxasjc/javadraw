//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Animation extends ObjectDrawShape implements Serializable {
    private final ArrayList labels;
    private final ArrayList specialRegions;
    private final AnimationType type;
    private final Color[] labelColors;
    private Particle[] particles;
    private final ObjectDrawShape basis;
    private int count;
    private int frame;
    private boolean cancelled;
    private boolean firstLabelRender;
    private final int dx;
    private final int dy;
    private final DrawingCanvas oldCanvas;
    private boolean attached;
    private static final List animations = Collections.synchronizedList(new ArrayList());
    private static final EventTimer timer = new EventTimer(new AnimationRunner(), "step");
    private static final Font LABEL_FONT;
    private static final Image[] blood;
    private static final Image[] smoke;
    private static final Image[] steam;
    private static final Image[] steam2;
    private static final Image[] explosion;
    private static final Image[] soot;
    private static final Image[] lightning;
    private static final Image[] spark;
    private static final Image[] green;
    private static final Image[] darkGreen;
    private static final Image[] limeGreen;
    private static final Image[] white;
    private static final Image[] purple;
    private static final Image[] rightArc;
    private static final Image[] leftArc;
    private static final Image[] pinkRightArc;
    private static final Image[] hailstone;
    private static final Image[] asteroid;
    private static Color defaultLabelColor;
    private static boolean defaultAttached;
    public static final AnimationType METEORS;
    public static final AnimationType METEOR;
    public static final AnimationType SLASH_RIGHT;
    public static final AnimationType SLASH_LEFT;
    public static final AnimationType SMOKE_BOMB;
    public static final AnimationType GLOW;
    public static final AnimationType DUSTY;
    public static final AnimationType SPARKLY;
    public static final AnimationType FROSTY;
    public static final AnimationType ICE_STORM;
    public static final AnimationType TREASURE;
    public static final AnimationType SHIELD;
    public static final AnimationType BURST;
    public static final AnimationType SPARKS;
    public static final AnimationType GOLD_SHOWER;
    public static final AnimationType SPLIT;
    public static final AnimationType SWIRL;
    public static final AnimationType SWARM;
    public static final AnimationType LIGHTNING;

    public static void setDefaultLabelColor(Color color) {
        defaultLabelColor = color;
    }

    public static void setAttachedByDefault(boolean attached) {
        defaultAttached = attached;
    }

    public static void cancelAll() {
        synchronized(animations) {
            Iterator iter = animations.iterator();

            while(iter.hasNext()) {
                ((Animation)iter.next()).cancel();
            }

        }
    }

    public void cancel() {
        this.cancelled = true;
    }

    private static Image[] makeImages(int[] pixels, int width, int height) {
        Image[] images = new Image[10];
        images[0] = new BufferedImage(width, height, 2);
        ((BufferedImage)images[0]).setRGB(0, 0, width, height, pixels, 0, width);

        for(int i = 1; i < 10; ++i) {
            for(int p = 0; p < pixels.length; ++p) {
                int alpha = ((pixels[p] & -16777216) >>> 4) * 7 << 1 & -16777216;
                pixels[p] = pixels[p] & 16777215 | alpha;
            }

            images[i] = new BufferedImage(width, height, 2);
            ((BufferedImage)images[i]).setRGB(0, 0, width, height, pixels, 0, width);
        }

        return images;
    }

    boolean step() {
        return !this.cancelled && this.type.step(this.particles, this.frame, this) | this.checkLabels(this.frame++);
    }

    private boolean isSpecial(double x, double y) {
        Location screenLoc = new Location(x + (double)this.getMyLocation().getX(), y + (double)this.getMyLocation().getY());

        for(int i = 0; i < this.specialRegions.size(); ++i) {
            DrawableInterface region = (DrawableInterface)this.specialRegions.get(i);
            if (region.contains(screenLoc)) {
                return true;
            }
        }

        return false;
    }

    public void start() {
        this.particles = this.type.createParticles(this);
        this.frame = 0;
        this.step();
        if (!animations.contains(this)) {
            animations.add(this);
        }

        if (this.getCanvas() == null) {
            this.addToCanvas(this.oldCanvas);
        }

        if (!timer.isRunning()) {
            timer.start(0.02, 0.02);
        }

    }

    public Animation fireOne(DrawableInterface object) {
        return this.type.fireOne(this, object);
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int n) {
        this.count = n;
    }

    public void addSpecialRegion(DrawableInterface object) {
        this.specialRegions.add(object);
    }

    public void label(DrawableInterface object, String label) {
        this.label(object, label, 0);
    }

    public void label(DrawableInterface object, String label, int dt) {
        int i;
        while((i = label.indexOf(10)) != -1) {
            this.labels.add(new Label(label.substring(0, i), object, dt));
            dt += LABEL_FONT.getSize();
            label = label.substring(i + 1);
        }

        this.labels.add(new Label(label, object, dt));
    }

    public void setAttached(boolean attached) {
        if (this.attached = attached) {
            this.update();
        }

    }

    /** @deprecated */
    @Deprecated
    public void update() {
        super.update();
        if (this.attached) {
            Location myLocation = this.getMyLocation();
            Location basisLocation = this.basis.getMyLocation();
            if (!myLocation.equals(basisLocation)) {
                myLocation.translate((double)(basisLocation.getX() - myLocation.getX() + this.dx), (double)(basisLocation.getY() - myLocation.getY() + this.dy));
            }

        }
    }

    public Animation(AnimationType type, DrawableInterface object) {
        this((Particle[])null, type, object);
    }

    private Animation(Particle[] particles, AnimationType type, DrawableInterface object) {
        super(type.getLocation(object), false, (Color)null, object.getCanvas());
        this.labels = new ArrayList();
        this.specialRegions = new ArrayList();
        this.labelColors = new Color[10];
        this.count = 1;
        this.cancelled = false;
        this.firstLabelRender = true;
        this.attached = defaultAttached;
        this.type = type;
        if (object == null) {
            throw new IllegalArgumentException("Object to decorate cannot be null.");
        } else {
            this.oldCanvas = object.getCanvas();
            this.basis = (ObjectDrawShape)object;
            Location basisLoc = this.basis.getMyLocation();
            Location myLoc = this.getMyLocation();
            this.dx = myLoc.getX() - basisLoc.getX();
            this.dy = myLoc.getY() - basisLoc.getY();
            basisLoc.depend(this);
            this.particles = particles;
            this.setColor(defaultLabelColor);
            this.ready();
        }
    }

    private boolean checkLabels(int frame) {
        int earliestFrame = frame - 40;

        for(int i = 0; i < this.labels.size(); ++i) {
            if (((Label)this.labels.get(i)).t >= earliestFrame) {
                return true;
            }
        }

        return false;
    }

    private static Location getCenter(DrawableInterface object) {
        Rectangle2D bounds = ((ObjectDrawShape)object).getShape().getBounds2D();
        return new Location(bounds.getCenterX(), bounds.getCenterY());
    }

    private static Location getOrigin(DrawableInterface object) {
        return new Location(((ObjectDrawShape)object).getMyLocation());
    }

    private void renderLabels(Graphics2D g, int frame) {
        g.setFont(LABEL_FONT);
        if (this.firstLabelRender) {
            this.firstLabelRender = false;
            FontMetrics fm = g.getFontMetrics();

            for(int i = 0; i < this.labels.size(); ++i) {
                Label label = (Label)this.labels.get(i);
                label.offset = -fm.stringWidth(label.text) / 2;
            }
        }

        for(int i = 0; i < this.labels.size(); ++i) {
            Label label = (Label)this.labels.get(i);
            int dt = frame - label.t;
            if (dt >= 0 && dt < 40) {
                g.setColor(this.labelColors[dt > 30 ? dt - 30 : 0]);
                Location origin = getCenter(label.object);
                int x = origin.getX() + label.offset;
                int y = origin.getY() - dt;
                g.drawString(label.text, x, y);
            }
        }

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
        if (this.attached) {
            this.basis.moveTo(point.getDoubleX() - (double)this.dx, point.getDoubleY() - (double)this.dy);
        } else {
            super.moveTo(point);
        }

    }

    public void moveTo(double x, double y) {
        if (this.attached) {
            this.basis.moveTo(x - (double)this.dx, y - (double)this.dy);
        } else {
            super.moveTo(x, y);
        }

    }

    public void move(double dx, double dy) {
        if (this.attached) {
            this.basis.move(dx, dy);
        } else {
            super.move(dx, dy);
        }

    }

    public Color getColor() {
        return super.getColor();
    }

    public void setColor(Color c) {
        super.setColor(c);
        int RGB = c.getRGB() & 16777215;
        int alpha = c.getAlpha();
        this.labelColors[0] = c;

        for(int i = 0; i < 10; ++i) {
            this.labelColors[i] = new Color(RGB | alpha * (10 - i) / 10 << 24, true);
        }

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
        return false;
    }

    public String toString() {
        return "new Animation()";
    }

    /** @deprecated */
    @Deprecated
    public void draw(Graphics2D g) {
        if (this.frame != 0) {
            AffineTransform saveAT = g.getTransform();
            g.translate(this.getMyLocation().getX(), this.getMyLocation().getY());
            this.type.draw(this.particles, g, this);
            g.setTransform(saveAT);
            this.renderLabels(g, this.frame);
        }
    }

    /** @deprecated */
    @Deprecated
    public Shape makeShape() {
        return new GeneralPath();
    }

    public int getX() {
        return this.getMyLocation().getX();
    }

    public int getY() {
        return this.getMyLocation().getY();
    }

    public double getDoubleX() {
        return this.getMyLocation().getDoubleX();
    }

    public double getDoubleY() {
        return this.getMyLocation().getDoubleY();
    }

    public Location getLocation() {
        return this.getMyLocation();
    }

    static {
        LABEL_FONT = Text.DEFAULT_FONT.deriveFont(1, 24.0F);
        defaultLabelColor = DEFAULT_COLOR;
        defaultAttached = true;
        METEORS = new MeteorAnimationType();
        METEOR = new SingleMeteorAnimationType();
        SLASH_RIGHT = new BloodAnimationType(1);
        SLASH_LEFT = new BloodAnimationType(-1);
        SMOKE_BOMB = new SimpleAnimationType(40) {
            Particle[] createParticles(Animation animation) {
                Particle[] particles = new Particle[100];
                int i = 0;

                while(i < particles.length) {
                    double x = -25.0 + Math.random() * 50.0;
                    double y = -35.0 + Math.random() * 50.0;
                    if (!(x * x + y * y > 625.0)) {
                        particles[i++] = new Particle(x, y, 2.5 - 5.0 * Math.random(), 1.0 - 7.0 * Math.random(), (int)(Math.random() * 20.0), Animation.smoke[0]);
                    }
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                if (frame < 40 && !(particle.y > 30.0)) {
                    particle.vx = 0.8 * particle.vx + 2.0 * Math.random() - 1.0;
                    particle.vy = 0.8 * particle.vy + 2.0 * Math.random() - 0.75;
                    if (frame > 30) {
                        particle.image = Animation.smoke[frame - 30];
                    }
                } else {
                    particle.image = null;
                }

            }
        };
        GLOW = new SimpleAnimationType(60) {
            Location getLocation(DrawableInterface object) {
                return Animation.getOrigin(object);
            }

            Particle[] createParticles(Animation animation) {
                Particle[] particles = this.outline(animation.basis, 1.2, Animation.green[0]);

                for(int i = 0; i < particles.length; ++i) {
                    Particle p = particles[i];
                    p.t = (int)(Math.random() * 30.0);
                    p.vx = p.vy = 0.0;
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame >= 0 && frame < 30) {
                    if (frame < 20) {
                        particle.image = Animation.green[frame < 9 ? 9 - frame : 0];
                    } else if (frame == 20) {
                        particle.vx = Math.random() - 0.5;
                        particle.vy = -4.0 - 2.0 * Math.random();
                        particle.image = Animation.green[0];
                    } else {
                        particle.vx = 0.8 * particle.vx + 0.5 * Math.random() - 0.25;
                        particle.vy = 0.8 * particle.vy + 0.5 * Math.random() - 0.25;
                        particle.image = Animation.green[frame - 20];
                    }

                } else {
                    particle.image = null;
                }
            }
        };
        DUSTY = new SimpleAnimationType(90) {
            Location getLocation(DrawableInterface object) {
                return Animation.getOrigin(object);
            }

            Particle[] createParticles(Animation animation) {
                Particle[] particles = this.outline(animation.basis, 0.75, Animation.soot[0]);

                for(int i = 0; i < particles.length; ++i) {
                    Particle p = particles[i];
                    p.t = (int)(Math.random() * 20.0 + Math.random() * 20.0 + Math.random() * 20.0);
                    p.vx = p.vx * 0.3;
                    p.vy = p.vy * 0.3;
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame >= 0 && frame < 30) {
                    particle.image = Animation.soot[frame / 3];
                } else {
                    particle.image = null;
                }
            }
        };
        SPARKLY = new SimpleAnimationType(90) {
            Location getLocation(DrawableInterface object) {
                return Animation.getOrigin(object);
            }

            Particle[] createParticles(Animation animation) {
                Particle[] particles = this.outline(animation.basis, 0.75, Animation.spark[0]);

                for(int i = 0; i < particles.length; ++i) {
                    Particle p = particles[i];
                    p.t = (int)(Math.random() * 20.0 + Math.random() * 20.0 + Math.random() * 20.0);
                    p.vx = p.vx * 0.3;
                    p.vy = p.vy * 0.3;
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame >= 0 && frame < 30) {
                    particle.image = Animation.spark[frame / 3];
                } else {
                    particle.image = null;
                }
            }
        };
        FROSTY = new SimpleAnimationType(90) {
            Location getLocation(DrawableInterface object) {
                return Animation.getOrigin(object);
            }

            Particle[] createParticles(Animation animation) {
                Particle[] particles = this.outline(animation.basis, 0.75, Animation.white[0]);

                for(int i = 0; i < particles.length; ++i) {
                    Particle p = particles[i];
                    p.t = (int)(Math.random() * 20.0 + Math.random() * 20.0 + Math.random() * 20.0);
                    p.vx = p.vx * 0.3;
                    p.vy = p.vy * 0.3;
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame >= 0 && frame < 30) {
                    particle.image = Animation.white[frame / 3];
                } else {
                    particle.image = null;
                }
            }
        };
        ICE_STORM = new SimpleAnimationType(70) {
            Particle[] createParticles(Animation animation) {
                Particle[] particles = new Particle[600];
                int i = 0;

                while(true) {
                    while(i < particles.length) {
                        double angle = Math.random() * Math.PI * 2.0;
                        double r = 0.5 + Math.random();
                        double target_x = 50.0 * Math.cos(angle) * r;
                        double target_y = 60.0 * Math.sin(angle) * r;
                        double vx = 8.0 * Math.random() - 4.0;
                        double vy = 8.0 + 4.0 * Math.random();
                        int t = (int)(Math.random() * 10.0) * 4;
                        double x = target_x - 20.0 * vx;
                        double y = target_y - 20.0 * vy;
                        particles[i++] = new Particle(x, y, vx, vy, t, Animation.hailstone[0]);
                        int e;
                        if (animation.isSpecial(target_x, target_y)) {
                            for(e = 0; e < 9; ++e) {
                                double rad = Math.random() * Math.PI * 2.0;
                                double speed = Math.random() * 6.0 + 1.0;
                                particles[i++] = new Particle(target_x, target_y, speed * Math.sin(rad), speed * Math.cos(rad), t + 22 + (int)Math.random() * 2, Animation.steam[0]);
                            }
                        } else {
                            for(e = 0; e < 9; ++e) {
                                particles[i++] = new Particle(target_x, target_y, Math.random() * 6.0 - 3.0, -5.0 - 2.0 * Math.random(), t + 21, Animation.white[0]);
                            }
                        }
                    }

                    return particles;
                }
            }

            void update(Particle particle, int frame) {
                frame -= particle.t & -4;
                if (frame >= 0 && frame < 20) {
                    switch (particle.t & 3) {
                        case 1:
                            if (frame >= 10) {
                                particle.vy = 0.0;
                                particle.vx = particle.vx * 0.85;
                            } else {
                                particle.vy = particle.vy + 1.0;
                            }

                            particle.image = frame > 10 ? Animation.white[frame - 10] : Animation.white[0];
                            return;
                        case 2:
                        case 3:
                            particle.vx = 0.8 * particle.vx + Math.random() * 1.0 - 0.5;
                            particle.vy = 0.8 * particle.vy + Math.random() * 1.0 - 0.5;
                            particle.image = ((particle.t & 3) == 2 ? Animation.steam : Animation.steam2)[frame / 2];
                            return;
                        default:
                            if (frame >= 0 && frame < 20) {
                                particle.image = frame < 9 ? Animation.hailstone[9 - frame] : Animation.hailstone[0];
                            } else {
                                particle.image = null;
                            }

                    }
                } else {
                    particle.image = null;
                }
            }
        };
        TREASURE = new SimpleAnimationType(50) {
            Particle[] createParticles(Animation animation) {
                Particle[] particles = new Particle[100];

                double x;
                double y;
                for(int i = 0; i < particles.length; particles[i++] = new Particle(-10.0 + x - 0.5 * y, -8.0 + y + 0.35 * x, (int)(Math.random() * 50.0), Animation.spark[0])) {
                    x = Math.random() * 22.0;
                    y = Math.random() * 10.0;
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                int dt = particle.t - frame;
                if (dt < 0) {
                    particle.image = null;
                } else {
                    particle.image = dt < 9 ? Animation.spark[9 - dt] : Animation.spark[0];
                }

            }
        };
        SHIELD = new SimpleAnimationType(70, -5) {
            Particle[] createParticles(Animation animation) {
                Particle[] particles = new Particle[200];
                int i = 0;

                while(i < particles.length) {
                    double phi = (Math.random() - 0.5) * Math.PI;
                    double circ = Math.random();
                    double cos = Math.cos(phi);
                    if (!(circ > cos)) {
                        double y = 25.0 * Math.sin(phi);
                        double x = 25.0 * cos * Math.sin(circ * Math.PI * 2.0 / cos);
                        particles[i++] = new Particle(x, y, (int)(Math.random() * 40.0), Animation.darkGreen[0]);
                    }
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame > 0 && frame < 30) {
                    double sin = particle.y / 25.0;
                    double cos = Math.sqrt(1.0 - sin * sin);
                    double dCirc = 2.0 * cos * Math.cos(Math.PI * particle.x / (50.0 * cos));
                    particle.x = particle.x + dCirc;
                    if (frame <= 10) {
                        particle.image = Animation.darkGreen[10 - frame];
                    } else if (frame >= 20) {
                        particle.image = Animation.darkGreen[frame - 20];
                    }

                } else {
                    particle.image = null;
                }
            }
        };
        BURST = new SimpleAnimationType(60, -5) {
            Particle[] createParticles(Animation animation) {
                Particle[] particles = new Particle[100];
                int i = 0;

                while(i < particles.length) {
                    double phi = (Math.random() - 0.5) * Math.PI;
                    double circ = Math.random();
                    double cos = Math.cos(phi);
                    if (!(circ > cos)) {
                        double y = 20.0 * Math.sin(phi);
                        double x = 20.0 * cos * Math.sin(circ * Math.PI * 2.0 / cos);
                        particles[i++] = new Particle(x, y, (int)(Math.random() * 30.0), Animation.limeGreen[0]);
                    }
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame > 0 && frame < 30) {
                    if (frame <= 10) {
                        particle.image = Animation.limeGreen[10 - frame];
                    } else if (frame > 15) {
                        particle.vx = 0.05 * particle.x;
                        particle.vy = 0.05 * particle.y;
                        particle.image = Animation.limeGreen[frame <= 20 ? 0 : frame - 20];
                    }

                } else {
                    particle.image = null;
                }
            }
        };
        SPARKS = new SimpleAnimationType(40) {
            Particle[] createParticles(Animation animation) {
                Particle[] particles = new Particle[50];

                double x;
                double y;
                for(int i = 0; i < particles.length; particles[i++] = new Particle(x, y, (int)(Math.random() * 20.0), Animation.spark[0])) {
                    x = -20.0 + Math.random() * 40.0;
                    y = 25.0 + Math.random() * 10.0;
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame > 0 && frame < 20) {
                    particle.vx = 0.8 * particle.vx + 2.0 * Math.random() - 1.0;
                    particle.vy = 0.8 * particle.vy - 3.0 * Math.random() + 1.0;
                    particle.image = Animation.spark[frame <= 10 ? 0 : frame - 10];
                } else {
                    particle.image = null;
                }
            }
        };
        GOLD_SHOWER = new SimpleAnimationType(60) {
            Particle[] createParticles(Animation animation) {
                Particle[] particles = new Particle[100];

                double x;
                double y;
                for(int i = 0; i < particles.length; particles[i++] = new Particle(x, y, (int)(Math.random() * 30.0), Animation.spark[0])) {
                    x = -25.0 + Math.random() * 50.0;
                    y = -45.0 + Math.random() * 50.0;
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame > 0 && frame < 30) {
                    particle.vx = 0.8 * particle.vx + 0.5 * Math.random() - 0.25;
                    particle.vy = 0.8 * particle.vy + 0.2;
                    if (frame <= 10) {
                        particle.image = Animation.spark[10 - frame];
                    } else if (frame > 20) {
                        particle.image = Animation.spark[frame - 20];
                    }

                } else {
                    particle.image = null;
                }
            }
        };
        SPLIT = new SimpleAnimationType(90) {
            Particle[] createParticles(Animation animation) {
                Particle[] particles = new Particle[120];

                double y;
                for(int i = 0; i < particles.length; particles[i++] = new Particle(1.0, y - 40.0, 10 * (i / 40) + (int)((70.0 - y) / 7.0), Animation.rightArc[0])) {
                    y = 4.0 + Math.random() * 62.0;
                    particles[i++] = new Particle(-1.0, y - 40.0, 10 * (i / 40) + (int)((70.0 - y) / 7.0), Animation.leftArc[0]);
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame > 0 && frame < 40) {
                    if (particle.x < 0.0) {
                        if (frame <= 10) {
                            particle.image = Animation.leftArc[10 - frame];
                        } else if (frame > 30) {
                            particle.image = Animation.leftArc[frame - 30];
                        } else {
                            particle.vx = particle.vx - 0.2 * Math.random();
                        }
                    } else if (frame <= 10) {
                        particle.image = Animation.rightArc[10 - frame];
                    } else if (frame > 30) {
                        particle.image = Animation.rightArc[frame - 30];
                    } else {
                        particle.vx = particle.vx + 0.2 * Math.random();
                    }

                } else {
                    particle.image = null;
                }
            }
        };
        SWIRL = new SimpleAnimationType(100) {
            Particle[] createParticles(Animation animation) {
                Particle[] particles = new Particle[150];
                int i = 0;

                while(i < particles.length) {
                    double y = -6.0 + Math.random() * 32.0;
                    double x = -24.0 + Math.random() * 10.0;
                    int t = (int)(Math.random() * 5.0) * 10;

                    for(int p = 0; p < 10; ++t) {
                        particles[i++] = new Particle(x, y, 0.0, -1.0, t++, Animation.pinkRightArc[0]);
                        ++p;
                    }
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame <= 0) {
                    particle.image = null;
                } else {
                    int f = particle.t % 10;
                    if (frame > 40) {
                        f += frame - 40;
                        if (f > 9) {
                            particle.image = null;
                            return;
                        }
                    }

                    if (particle.x > 20.0) {
                        particle.x = -20.0;
                    }

                    particle.vx = 2.0 * Math.cos(particle.x * Math.PI / 50.0);
                    particle.image = Animation.pinkRightArc[f];
                }
            }
        };
        SWARM = new SimpleAnimationType(70) {
            Particle[] createParticles(Animation animation) {
                Particle[] particles = new Particle[80];

                double x;
                double y;
                for(int i = 0; i < particles.length; particles[i++] = new Particle(x, y, (int)(Math.random() * 30.0), Animation.soot[0])) {
                    x = -25.0 + Math.random() * 50.0;
                    y = -30.0 + Math.random() * 60.0;
                }

                return particles;
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame < 0) {
                    particle.image = null;
                } else {
                    particle.vx = 0.7 * particle.vx;
                    particle.vy = 0.7 * particle.vy + 0.1;
                    if (Math.random() < 0.1) {
                        particle.vx = particle.vx + (6.0 * Math.random() - 3.0 - particle.x / 20.0);
                        particle.vy = particle.vy + (6.0 * Math.random() - 5.0 - particle.y / 20.0);
                    }

                    if (frame <= 9) {
                        particle.image = Animation.soot[9 - frame];
                    } else if (frame > 30) {
                        if (frame < 40) {
                            particle.image = Animation.soot[frame - 30];
                        } else if (frame == 40) {
                            particle.image = null;
                        }
                    }

                }
            }
        };
        LIGHTNING = new SimpleAnimationType(30) {
            Particle[] createParticles(Animation animation) {
                ArrayList particles = new ArrayList();
                particles.add(new Particle(0.0, -30.0, 9, Animation.purple[0]));
                int lineStart = 0;

                int y;
                for(y = -3; y > -207; y -= 3) {
                    int lastLineStart = lineStart;
                    lineStart = particles.size();
                    double branchProb = 0.2 / (double)(lineStart - lastLineStart);

                    for(int j = lastLineStart; j < lineStart; ++j) {
                        Particle p = (Particle)particles.get(j);
                        int t = p.t - (int)(Math.random() * 1.4);
                        if (t >= 0) {
                            double vx;
                            if (Math.random() < branchProb) {
                                vx = Math.random() + 1.0;
                                particles.add(new Particle(p.x + vx, (double)y, vx, 0.0, t, Animation.purple[0]));
                                vx = -Math.random() - 1.0;
                                particles.add(new Particle(p.x + vx, (double)y, vx, 0.0, t, Animation.purple[0]));
                            } else {
                                vx = p.vx + Math.random() - 0.5;
                                particles.add(new Particle(p.x + vx, (double)y, vx, 0.0, t, Animation.purple[0]));
                            }
                        }

                        p.vx = 0.0;
                    }
                }

                for(y = lineStart; y < particles.size(); ++y) {
                    ((Particle)particles.get(y)).vx = 0.0;
                }

                for(y = 0; y < 30; ++y) {
                    double angle = Math.random() * Math.PI * 2.0;
                    double speed = 5.0 + Math.random() * 2.0;
                    particles.add(new Particle(0.0, 0.0, speed * Math.sin(angle), speed * Math.cos(angle), 10, Animation.white[0]));
                }

                return (Particle[])((Particle[])particles.toArray(new Particle[particles.size()]));
            }

            void update(Particle particle, int frame) {
                frame -= particle.t;
                if (frame < 0) {
                    particle.image = null;
                } else if (particle.t == 10) {
                    particle.vx = 0.8 * particle.vx + 0.5 * Math.random() - 0.25;
                    particle.vy = 0.8 * particle.vy + 0.5 * Math.random() - 0.15;
                    particle.image = frame <= 10 ? Animation.white[0] : (frame > 19 ? null : Animation.white[frame - 10]);
                } else {
                    particle.image = frame < 10 ? Animation.purple[frame] : null;
                }

            }
        };
        int[] pixels = new int[]{0, 1727529217, 1071120898, 0, 1073676802, -1711341568, -1716781056, 1726218497, 1725104385, -1715142656, -1717436416, 1724121345, 0, 1726218497, 1722941697, 1066074626};
        blood = makeImages(pixels, 4, 4);
        pixels = new int[]{0, 0, 863993727, 0, 1283555713, 423114808, 431799484, 423114808, 863993727, 0, 436207615, 1288819153, 0, 863993727, 1291845631, 431799484, 872415231, 1283555713, 431799484, 436207615, 427128181, 0, 1291845631, 1286187433, 423114808, 1284871573, 860375112, 855769602, 863993727, 1288819153, 0, 869915097, 1284871573, 863993727, 872415231, 867612342, 867612342, 1279542340, 869915097, 427128181, 427128181, 1286187433, 0, 427128181, 867612342, 1288819153, 419759365, 867612342, 860375112, 419759365, 436207615, 427128181, 1291845631, 872415231, 1283555713, 1291845631, 1279542340, 869915097, 1286187433, 855769602, 436207615, 427128181, 1286187433, 423114808, 872415231, 1284871573, 869915097, 423114808, 0, 423114808, 0, 0, 855769602, 872415231, 860375112, 0, 867612342, 1284871573, 1291845631, 1284871573, 1288819153, 0, 0, 1279542340, 431799484, 1283555713, 860375112, 860375112, 855769602, 436207615, 0, 0, 1284871573, 0, 0, 419759365, 0, 1286187433, 0, 0};
        smoke = makeImages(pixels, 10, 10);
        pixels = new int[]{0, 0, 871826175, 872352985, 872352985, 0, 0, 0, 872350400, -2098459905, -2097214742, -2097214273, 872350400, 0, 871826175, -2097214273, -906025777, -47920, -906027617, -2097214742, 872350400, 872352985, -2097214273, -57698, -41258, -57880, -2097214273, 872352985, 872350400, -2097214742, -906032448, -55614, -906031384, -2098459905, 871826175, 0, 872350400, -2098459905, -2097214273, -2097214742, 872350400, 0, 0, 0, 871826175, 872352985, 872350400, 0, 0};
        lightning = makeImages(pixels, 7, 7);
        pixels = new int[]{37699391, 872546818, -2046688766, 1342243073, -2063465982, 838992386, 889324034, -1610481150, -1258291200, -754974720, -1526595070, 1996554497, 319161862, -2046688766, -218103808, -33554432, -754974720, 1828782337, 1275134209, -1610481150, -536870912, -134217728, -1258291200, 1291911425, 252184584, -1761476094, -1342177280, -1576926718, -1996357118, 2097217793, 0, 235473161, 838992386, 302450439, 822215170, 135204623};
        soot = makeImages(pixels, 6, 6);
        pixels = new int[]{0, 0, 872396345, 872397590, 872401415, 0, 0, 0, 872398890, 2147483393, 2147475745, 2147474689, 872401415, 0, 872397590, 2147475745, -855642610, -953, -856031470, 2147483393, 872397590, 872401415, 2147483393, -393454, -65671, -2276, 2147475745, 872401415, 872398890, 2147474689, -855643337, -4806, -855644667, 2147474689, 872396345, 0, 872401415, 2147483393, 2147475745, 2147483393, 872401415, 0, 0, 0, 872396345, 872397590, 872401415, 0, 0};
        spark = makeImages(pixels, 7, 7);
        pixels = new int[]{0, 2138439427, 2137259793, 0, 2132934411, -16711903, -443220223, 2134638343, 2136080153, -866451712, -10551552, 2133589767, 0, 2141454105, 2138701575, 0};
        green = makeImages(pixels, 4, 4);
        pixels = new int[]{0, 1275181579, 1275181579, 1275174667, 0, 1275181579, -1308379635, -452746485, -1308385782, 1275174667, 1275181579, -452746485, -16477173, -452820471, 1275165192, 1275174667, -1308385782, -452820471, -1308395767, 1275165192, 0, 1275174667, 1275165192, 1275165192, 0};
        darkGreen = makeImages(pixels, 5, 5);
        pixels = new int[]{0, 1282473729, 1282473729, 1279393537, 0, 1282473729, -1299448064, -445645048, -1302266112, 1279393537, 1282473729, -445645048, -9896185, -447873278, 1275657992, 1279393537, -1302266112, -447873278, -1304232192, 1275657992, 0, 1279393537, 1275657992, 1275657992, 0};
        limeGreen = makeImages(pixels, 5, 5);
        pixels = new int[]{0, 1291843318, 1291843318, 1291843318, 0, 1291843318, -1291848203, -436210187, -1291848203, 1291843318, 1291843318, -436210187, -2571, -436210187, 1291843318, 1291843318, -1291848203, -436210187, -1291848203, 1291843318, 0, 1291843318, 1291843318, 1291843318, 0};
        white = makeImages(pixels, 5, 5);
        pixels = new int[]{0, 0, 864618495, 864618495, 861995775, 0, 0, 0, 864618495, 2140342271, 2140342271, 2137850367, 861995775, 0, 864618495, 2140342271, -1298920961, -445840897, -865205249, 2137850367, 861995775, 864618495, 2140342271, -445840897, -9762561, -445840897, 2136540671, 860357375, 861995775, 2137850367, -865205249, -445840897, -1302593281, 2136540671, 860357375, 0, 861995775, 2137850367, 2136540671, 2136540671, 860357375, 0, 0, 0, 861995775, 860357375, 860357375, 0, 0};
        purple = makeImages(pixels, 7, 7);
        pixels = new int[]{-1308560897, -16583937, -16583937, -1308560897, 0, 0, 1711535103, -1727859969, -872156161, -16583937, -1308560897, 0, 855828722, 1711532531, -1727862542, -872159760, -16589593, -1308560897, 0, 855826152, 1711528676, -1727866397, -872163873, -16589593, 0, 855826152, 1711528676, -1727866397, -872163873, -16594991, 855823582, 1711526106, -1727870766, -872166443, -16594991, -1308569379, 1711523022, -1727874107, -872170298, -16599104, -1308573494, 0, -1308576324, -16599104, -16599104, -1308576064, 0, 0};
        rightArc = makeImages(pixels, 6, 8);
        pixels = new int[]{0, 0, -1308560897, -16583937, -16583937, -1308560897, 0, -1308560897, -16583937, -872156161, -1727859969, 1711535103, -1308560897, -16589593, -872159760, -1727862542, 1711532531, 855828722, -16589593, -872163873, -1727866397, 1711528676, 855826152, 0, -16594991, -872163873, -1727866397, 1711528676, 855826152, 0, -1308569379, -16594991, -872166443, -1727870766, 1711526106, 855823582, 0, -1308573494, -16599104, -872170298, -1727874107, 1711523022, 0, 0, -1308576064, -16599104, -16599104, -1308576324};
        leftArc = makeImages(pixels, 6, 8);
        pixels = new int[]{-1292828417, -916737, -916737, -1292828417, 0, 0, 1727202303, -1712192769, -856488961, -916737, -1292828417, 0, 870843122, 1726546931, -1712848142, -857406480, -2358553, -1292828417, 0, 870187752, 1725563876, -1713831197, -858455073, -2358553, 0, 870187752, 1725563876, -1713831197, -858455073, -3734831, 869532382, 1724908506, -1714945326, -859110443, -3734831, -1294991139, 1724122062, -1715797307, -860093498, -4783424, -1296039734, 0, -1296760644, -4783424, -4783424, -1296695104, 0, 0};
        pinkRightArc = makeImages(pixels, 6, 8);
        pixels = new int[]{0, 0, 2147481075, -855640589, -855640589, 2147481075, 0, 0, 0, 2147481075, -855640589, -2574, -2574, -860562689, 2142624255, 0, 2147481075, -855640589, -2574, -3014670, -3014670, -4859393, -860562689, 2142615551, -855640589, -2574, -3014670, -5177345, -4462849, -5650433, -4868353, -860571649, -855640589, -2574, -3014670, -4462849, -5645057, -5650433, -4868353, -860571649, 2147481075, -860562689, -4859393, -5650433, -5650433, -4868353, -860571649, 2142615551, 0, 2142624255, -860562689, -4868353, -4868353, -860571649, 2142615551, 0, 0, 0, 2142615551, -860571649, -860571649, 2142615551, 0, 0};
        hailstone = makeImages(pixels, 8, 8);
        pixels = new int[]{0, 0, 0, 855638015, 855638015, 855638015, 855638015, 855638015, 0, 0, 0, 0, 0, 0, 855638015, 1711276031, Integer.MAX_VALUE, Integer.MAX_VALUE, 1711276031, 1291845631, 855638015, 0, 0, 0, 0, 855638015, 1711276031, -1728053249, -855638017, -1728053249, -1728053249, 1711276031, 1291845631, 855638015, 0, 0, 855638015, 1711276031, Integer.MAX_VALUE, -1291845633, -855638017, -1291845633, -855638017, Integer.MAX_VALUE, 1711276031, 1711276031, 855638015, 0, 855638015, 1711276031, Integer.MAX_VALUE, -1728053249, -855638017, -855638017, -1291845633, -1728053249, -1728053249, Integer.MAX_VALUE, 1711276031, 855638015, 0, 855638015, 1711276031, Integer.MAX_VALUE, -855638017, -1291845633, -1291845633, -855638017, -1291845633, -1728053249, Integer.MAX_VALUE, 855638015, 0, 855638015, 1711276031, Integer.MAX_VALUE, -1728053249, Integer.MAX_VALUE, -1728053249, -855638017, -855638017, -1291845633, -1728053249, 855638015, 855638015, Integer.MAX_VALUE, -1291845633, -855638017, -1728053249, 1711276031, Integer.MAX_VALUE, -1728053249, -1291845633, -1728053249, Integer.MAX_VALUE, 855638015, 855638015, Integer.MAX_VALUE, -1291845633, -1728053249, Integer.MAX_VALUE, 1291845631, 1711276031, Integer.MAX_VALUE, -1728053249, Integer.MAX_VALUE, 1711276031, 855638015, 855638015, 1711276031, Integer.MAX_VALUE, Integer.MAX_VALUE, 1711276031, 855638015, 1291845631, 1711276031, 1711276031, 1711276031, 855638015, 0, 0, 855638015, 1711276031, 1711276031, 1291845631, 855638015, 855638015, 1291845631, 1291845631, 855638015, 0, 0, 0, 0, 855638015, 855638015, 855638015, 0, 0, 855638015, 855638015, 0, 0, 0};
        steam = makeImages(pixels, 12, 12);
        pixels = new int[]{855638015, 855638015, 0, 0, 0, 855638015, 855638015, 855638015, 855638015, 0, 0, 0, 855638015, 0, 0, 855638015, 855638015, 1711276031, Integer.MAX_VALUE, Integer.MAX_VALUE, 1711276031, 855638015, 0, 0, 0, 855638015, 855638015, 1291845631, 1711276031, Integer.MAX_VALUE, -1728053249, -1728053249, Integer.MAX_VALUE, 1711276031, 855638015, 0, 0, 855638015, 1291845631, 1711276031, Integer.MAX_VALUE, -1728053249, -1291845633, -1291845633, -1728053249, Integer.MAX_VALUE, 1711276031, 855638015, 855638015, 1711276031, Integer.MAX_VALUE, Integer.MAX_VALUE, -1291845633, -1291845633, -855638017, -855638017, -1291845633, Integer.MAX_VALUE, 1711276031, 855638015, 855638015, Integer.MAX_VALUE, -1728053249, -1291845633, -1728053249, -1728053249, -1728053249, -1291845633, -1728053249, 1711276031, 855638015, 0, 855638015, Integer.MAX_VALUE, -1291845633, -855638017, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, -1291845633, -855638017, -1728053249, 1711276031, 855638015, 855638015, 1711276031, Integer.MAX_VALUE, Integer.MAX_VALUE, 1711276031, 1711276031, -1728053249, -1728053249, -1291845633, -1291845633, Integer.MAX_VALUE, 855638015, 855638015, 855638015, 855638015, 1291845631, 1711276031, 1711276031, Integer.MAX_VALUE, -1291845633, -855638017, -855638017, -1728053249, 855638015, 0, 855638015, 0, 855638015, 1291845631, 1711276031, Integer.MAX_VALUE, -1291845633, -855638017, -1291845633, Integer.MAX_VALUE, 855638015, 855638015, 855638015, 0, 0, 855638015, 1291845631, 1711276031, Integer.MAX_VALUE, -1728053249, -1728053249, 1711276031, 855638015, 0, 0, 855638015, 0, 0, 855638015, 855638015, 855638015, 855638015, 855638015, 855638015, 0};
        steam2 = makeImages(pixels, 12, 12);
        pixels = new int[]{0, 0, 0, 0, 118624786, 308042844, 0, 0, 0, 0, 0, 0, 0, 0, 50315199, 1432048978, -884457409, -431669443, -1405076163, 625952840, 0, 0, 0, 0, 0, 0, -1956752564, -11251892, -9937060, -10134696, -11450815, -648589495, 659245886, 0, 0, 0, 46120895, 1834635340, -10791855, -9345181, -8555669, -8292752, -11451586, -11450560, -499831252, 1294543138, 0, 0, 456998205, -194814888, -9410204, -9344925, -8161168, -9806248, -11713987, -12633803, -13817303, -81386719, 841359649, 0, 506930717, -193696667, -8226444, -9016476, -10267057, -11581630, -12305094, -13489110, -13554646, -14804199, -2061691372, 0, 508049207, -177643686, -10201005, -10528942, -10726579, -11450300, -12370886, -12896972, -13423059, -14606562, -937090784, 73359199, 171917119, -649445060, -10199463, -10528684, -12305351, -12304579, -13225425, -13686230, -14343390, -14737891, -417325540, 236657435, 0, 1597978935, -61715640, -13094867, -14080476, -13817303, -13817560, -14541282, -14672355, -14738148, -1927801324, 0, 0, 0, 891560996, 1797069081, -887218916, -316794343, -266726124, -618849252, -1525081834, 1562122778, 186780427, 0, 0, 0, 0, 0, 69148447, 387649552, 454498071, 185273099, 0, 0, 0, 0};
        asteroid = makeImages(pixels, 12, 11);
        pixels = new int[]{0, 0, 0, 855612162, 872369425, 855601666, 872398870, 0, 0, 0, 0, 0, 872369425, 1711249411, 1711249411, 1711240961, 1711240961, 855601666, 0, 0, 0, 855612162, 1711249411, 2147457797, -1291872250, -1291881472, 2147460873, 1728001288, 855601666, 0, 855612162, 1728035344, 2147466517, -1291872250, -436233722, -436226026, -1291867126, 2147448065, 1728001288, 872398870, 855612162, 1711249411, -1291863024, -436233722, -9713, -35328, -436243200, -1291863024, 1711240961, 855601666, 872369425, 1711240961, -1291881472, -436247534, -35328, -39415, -436247534, -1291885047, 1728035344, 855599111, 855601666, 1728035344, 2147433747, -1291891703, -436243200, -436226026, -1291896048, 2147433747, 1711236616, 855599111, 0, 855601666, 1711240961, 2147448065, -1291889142, -1291885047, 2147443975, 1728001288, 872369425, 0, 0, 0, 855601666, 1728035344, 1728035344, 1711236616, 1728001288, 855599111, 0, 0, 0, 0, 0, 872398870, 855601666, 872369425, 855599111, 0, 0, 0};
        explosion = makeImages(pixels, 10, 10);
    }

    static class AnimationRunner {
        AnimationRunner() {
        }

        public void step() {
            HashSet canvases = new HashSet();
            synchronized(Animation.animations) {
                Iterator iter = Animation.animations.iterator();

                while(iter.hasNext()) {
                    Animation animation = (Animation)iter.next();
                    if (!animation.step()) {
                        iter.remove();
                        animation.removeFromCanvas();
                    } else {
                        canvases.add(animation.getCanvas());
                    }
                }

                if (Animation.animations.isEmpty()) {
                    Animation.timer.stop();
                }
            }

            Iterator iter = canvases.iterator();

            while(iter.hasNext()) {
                DrawingCanvas canvas = (DrawingCanvas)iter.next();
                if (canvas != null) {
                    canvas.update();
                }
            }

        }
    }

    private static class Particle {
        private double x;
        private double y;
        private double vx;
        private double vy;
        private int t;
        private double dx;
        private double dy;
        private Image image;

        public Particle(double x, double y, int t, Image image) {
            this.x = x;
            this.y = y;
            this.t = t;
            this.setImage(image);
        }

        public Particle(double x, double y, double vx, double vy, int t, Image image) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.t = t;
            this.setImage(image);
        }

        public void setImage(Image image) {
            if (image == null) {
                this.dx = this.dy = 0.0;
            } else {
                this.dx = 0.5 * (double)image.getWidth((ImageObserver)null);
                this.dy = 0.5 * (double)image.getHeight((ImageObserver)null);
            }

            this.image = image;
        }
    }

    private static class Label {
        private final String text;
        private final int t;
        private final DrawableInterface object;
        private int offset;

        public Label(String text, DrawableInterface object, int t) {
            this.text = text;
            this.t = t;
            this.object = object;
        }
    }

    private abstract static class SimpleAnimationType extends AnimationType {
        private final int yOffset;
        private final int length;

        public SimpleAnimationType(int length) {
            this(length, 0);
        }

        public SimpleAnimationType(int length, int yOffset) {
            super();
            this.length = length;
            this.yOffset = yOffset;
        }

        Location getLocation(DrawableInterface object) {
            Location loc = Animation.getCenter(object);
            loc.translate(0.0, (double)this.yOffset);
            return loc;
        }

        public void draw(Particle[] particles, Graphics2D g, Animation animation) {
            if (animation.frame < this.length) {
                for(int i = 0; i < particles.length; ++i) {
                    this.draw(g, particles[i]);
                }

            }
        }

        abstract Particle[] createParticles(Animation var1);

        abstract void update(Particle var1, int var2);

        boolean step(Particle[] particles, int frame, Animation animation) {
            for(int i = 0; i < particles.length; ++i) {
                Particle particle = particles[i];
                this.update(particle, frame);
                if (particle.image != null) {
                    particle.x = particle.x + particle.vx;
                    particle.y = particle.y + particle.vy;
                }
            }

            return frame < this.length;
        }
    }

    private static class MeteorAnimationType extends SimpleAnimationType {
        public MeteorAnimationType() {
            super(0);
        }

        Animation fireOne(Animation animation, DrawableInterface object) {
            int c = animation.getCount();
            --c;
            animation.setCount(c);
            Particle[] p = new Particle[11];
            System.arraycopy(animation.particles, 11 * c, p, 0, 11);
            Animation anim = new Animation(p, Animation.METEOR, animation.basis);
            anim.addSpecialRegion(object);
            return anim;
        }

        Particle[] createParticles(Animation animation) {
            Particle[] particles = new Particle[11 * animation.count];
            int p = 0;

            for(int i = 0; p < animation.count; ++p) {
                double angle = Math.random() * Math.PI * 2.0;
                double r = Math.random() * 40.0;
                double speed = Math.sqrt(16.0 - 0.01 * r * r);
                double direction = Math.random() * Math.PI * 2.0;
                particles[i++] = new Particle(r * Math.sin(angle), r * Math.cos(angle), speed * Math.sin(direction), speed * Math.cos(direction), (int)(Math.random() * 4.0), Animation.asteroid[0]);

                for(int c = 0; c < 10; ++c) {
                    particles[i++] = new Particle(0.0, 0.0, c, Animation.explosion[0]);
                }
            }

            return particles;
        }

        boolean step(Particle[] particles, int frame, Animation animation) {
            int drawLength = 11 * animation.getCount();
            if (drawLength <= 0) {
                return false;
            } else {
                int i;
                for(i = 0; i < drawLength; i += 11) {
                    Particle particle = particles[i];
                    if (particle != null) {
                        particle.vx = particle.vx + -0.01 * particle.x;
                        particle.vy = particle.vy + -0.01 * particle.y;
                        double d = particle.vx * particle.x + particle.vy * particle.y;
                        particle = particles[i + 10];

                        for(int c = 9; c >= 0; --c) {
                            Particle prev = particles[i + c];
                            particle.x = prev.x;
                            particle.y = prev.y;
                            particle.t = prev.t;
                            particle = prev;
                        }

                        particle.x = particle.x + particle.vx;
                        particle.y = particle.y + particle.vy;
                        if ((particle.t & 1) == 1 != d > 0.0) {
                            particle.t = particle.t + 1 & 3;
                        }
                    }
                }

                for(i = 0; i < particles.length; i += 11) {
                    for(int c = 1; c <= 10; ++c) {
                        Particle particle = particles[i + c];
                        int t = c - 1;
                        if (frame < 10) {
                            t += 10 - frame;
                            particle.image = t < 10 ? Animation.explosion[t] : null;
                        } else {
                            particle.image = Animation.explosion[t];
                        }
                    }

                    particles[i].image = Animation.asteroid[frame < 9 ? 9 - frame : 0];
                }

                return true;
            }
        }

        public void draw(Particle[] particles, Graphics2D g, Animation animation) {
            int drawLength = 11 * animation.getCount();

            int dx;
            for(dx = 0; dx < drawLength; ++dx) {
                Particle particle = particles[dx];
                if (particle.t < 2) {
                    this.draw(g, particle);
                }
            }

            dx = animation.getMyLocation().getX();
            int dy = animation.getMyLocation().getY();
            g.translate(-dx, -dy);
            animation.basis.draw(g);
            g.translate(dx, dy);

            for(int i = 0; i < drawLength; ++i) {
                Particle particle = particles[i];
                if (particle.t >= 2) {
                    this.draw(g, particle);
                }
            }

        }

        void update(Particle particle, int frame) {
        }
    }

    private static class SingleMeteorAnimationType extends SimpleAnimationType {
        public SingleMeteorAnimationType() {
            super(40);
        }

        Particle[] createParticles(Animation animation) {
            Particle[] particles = new Particle[100];
            if (animation.specialRegions.isEmpty()) {
                throw new IllegalStateException("You must use addSpecialRegion() to specify a meteor's target.");
            } else {
                Location target = Animation.getCenter((DrawableInterface)animation.specialRegions.get(0));
                double tx = (double)(target.getX() - animation.getMyLocation().getX());
                double ty = (double)(target.getY() - animation.getMyLocation().getY());
                Particle accelerateParticle = particles[0] = new Particle(tx, ty, 1, (Image)null);
                if (animation.particles != null) {
                    System.arraycopy(animation.particles, 0, particles, 1, 11);
                } else {
                    double t = Math.sqrt(tx * tx + ty * ty);
                    double ox = -20.0 * tx / t + Math.random() * 40.0 - 20.0;
                    double oy = -20.0 * ty / t + Math.random() * 40.0 - 20.0;
                    double o = Math.sqrt(ox * ox + oy * oy);
                    double ovx = -3.0 * ox / o;
                    double ovy = -3.0 * oy / o;
                    Particle prev = particles[1] = new Particle(ox, oy, ovx, ovy, 0, Animation.asteroid[0]);

                    for(int i = 2; i <= 11; ++i) {
                        prev = particles[i] = new Particle(prev.x - ovx, prev.y - ovy, 0, Animation.explosion[i - 2]);
                    }
                }

                Particle head = particles[1];
                double vAvgX = (tx - head.x) / 20.0;
                double vAvgY = (ty - head.y) / 20.0;
                accelerateParticle.vx = (vAvgX - head.vx) / 10.0;
                accelerateParticle.vy = (vAvgY - head.vy) / 10.0;
                return particles;
            }
        }

        boolean step(Particle[] particles, int frame, Animation animation) {
            if (frame < 20) {
                int i = 11;

                while(i > 1) {
                    Particle p = particles[i--];
                    p.x = particles[i].x;
                    p.y = particles[i].y;
                }

                Particle head = particles[1];
                head.x = head.x + (head.vx = head.vx + particles[0].vx);
                head.y = head.y + (head.vy = head.vy + particles[0].vy);
            } else {
                if (frame >= 40) {
                    return false;
                }

                if (frame == 20) {
                    double px = particles[0].x;
                    double py = particles[0].y;

                    for(int i = 0; i < particles.length; ++i) {
                        double angle = Math.random() * Math.PI * 2.0;
                        double speed = Math.random() * 2.0 + 5.0;
                        particles[i] = new Particle(px, py, speed * Math.sin(angle), speed * Math.cos(angle), 0, Animation.explosion[0]);
                    }
                }

                Image image = Animation.explosion[frame <= 30 ? 0 : frame - 30];

                for(int i = 0; i < particles.length; ++i) {
                    Particle p = particles[i];
                    p.x = p.x + (p.vx = p.vx * 0.9 + 0.5 * Math.random() - 0.25);
                    p.y = p.y + (p.vy = p.vy * 0.9 + 0.5 * Math.random() - 0.25);
                    p.image = image;
                }
            }

            return true;
        }

        void update(Particle particle, int frame) {
        }
    }

    private abstract static class AnimationType {
        private AnimationType() {
        }

        Animation fireOne(Animation animation, DrawableInterface object) {
            return null;
        }

        private void accum(int[] pixels, int[] accum, int offset) {
            int var10001;
            int source;
            int dest;
            if (offset < 0) {
                source = 0;

                for(dest = -offset; dest < pixels.length; accum[var10001] += pixels[source++] >>> 24) {
                    var10001 = dest++;
                }
            } else {
                source = offset;

                for(dest = 0; source < pixels.length; accum[var10001] += pixels[source++] >>> 24) {
                    var10001 = dest++;
                }
            }

        }

        private BufferedImage getImage(VisibleImage image) {
            Image im = image.getImage();
            return im instanceof BufferedImage ? (BufferedImage)im : VisibleImage.createBufferedCopy(im);
        }

        Particle[] outline(ObjectDrawShape basis, double separation, Image image) {
            ArrayList particles = new ArrayList();
            if (basis instanceof VisibleImage) {
                BufferedImage target = this.getImage((VisibleImage)basis);
                int w = target.getWidth((ImageObserver)null);
                int h = target.getHeight((ImageObserver)null);
                int[] pixels = new int[w * h];
                int[] accum = new int[w * h];
                target.getRGB(0, 0, w, h, pixels, 0, w);
                this.accum(pixels, accum, 0);
                this.accum(pixels, accum, 1);
                this.accum(pixels, accum, w);
                this.accum(pixels, accum, w + 1);
                double prob = 0.5 / separation;

                int x;
                int a;
                int b;
                for(x = 1; x < h - 1; ++x) {
                    for(a = 1; a < w - 1; ++a) {
                        b = a + w * x;
                        int a1 = accum[b];
                        if (a1 >= 20 && a1 <= 1000 && Math.random() < prob) {
                            double gx = 0.7 * (double)(-accum[b - w - 1] - accum[b + w - 1] + accum[b - w + 1] + accum[b + w + 1]) - (double)accum[b - 1] + (double)accum[b + 1];
                            double gy = 0.7 * (double)(-accum[b - w - 1] + accum[b + w - 1] - accum[b - w + 1] + accum[b + w + 1]) - (double)accum[b - w] + (double)accum[b + w];
                            double g = -1.0 / Math.sqrt(gx * gx + gy * gy);
                            particles.add(new Particle((double)a1, (double)x, g * gx, g * gy, 0, image));
                        }
                    }
                }

                for(x = 0; x < h; ++x) {
                    a = accum[x * w];
                    if (a >= 20 && a <= 1000 && Math.random() < prob) {
                        particles.add(new Particle(0.0, (double)x, -1.0, 0.0, 0, image));
                    }

                    b = accum[x * w + w - 1];
                    if (b >= 20 && b <= 1000 && Math.random() < prob) {
                        particles.add(new Particle((double)(w - 1), (double)x, 1.0, 0.0, 0, image));
                    }
                }

                for(x = 0; x < w; ++x) {
                    a = accum[x];
                    if (a >= 20 && a <= 1000 && Math.random() < prob) {
                        particles.add(new Particle((double)x, 0.0, 0.0, -1.0, 0, image));
                    }

                    b = accum[(h - 1) * w + x];
                    if (b >= 20 && b <= 1000 && Math.random() < prob) {
                        particles.add(new Particle((double)x, (double)(h - 1), 0.0, 1.0, 0, image));
                    }
                }
            } else {
                Location origin = Animation.getOrigin(basis);
                double dx = -origin.getDoubleX();
                double dy = -origin.getDoubleY();
                PathMeasurer.Point[] points = PathMeasurer.getPoints(basis.getShape(), separation);

                for(int i = 0; i < points.length; ++i) {
                    PathMeasurer.Point p = points[i];
                    particles.add(new Particle(p.x + dx, p.y + dy, p.nx, p.ny, 0, image));
                }
            }

            return (Particle[])((Particle[])particles.toArray(new Particle[particles.size()]));
        }

        void draw(Graphics2D g, Particle particle) {
            if (particle != null && particle.image != null) {
                g.drawImage(particle.image, (int)(particle.x - particle.dx + 0.5), (int)(particle.y - particle.dy + 0.5), (ImageObserver)null);
            }

        }

        abstract void draw(Particle[] var1, Graphics2D var2, Animation var3);

        abstract Location getLocation(DrawableInterface var1);

        abstract Particle[] createParticles(Animation var1);

        abstract boolean step(Particle[] var1, int var2, Animation var3);
    }

    private static class BloodAnimationType extends SimpleAnimationType {
        private int dir;

        private BloodAnimationType(int dir) {
            super(40);
            this.dir = dir;
        }

        Particle[] createParticles(Animation animation) {
            Particle[] particles = new Particle[50];
            double slope = 0.5 * Math.cos(Math.random() * Math.PI);
            double intercept = 20.0 * (1.0 - slope + Math.random()) - 30.0;
            double offset = Math.random() * 8.0 + 1.0 - 25.0;

            for(int frame = 0; frame < 10; ++frame) {
                for(int i = 0; i < 5; ++i) {
                    double x = 20.0 + (double)this.dir * (Math.random() * 4.0 + (double)(4 * frame) - 20.0);
                    double y = x * slope + intercept + Math.random() * 3.0 - 1.5;
                    particles[frame * 5 + i] = new Particle(x + offset, y, frame, Animation.blood[0]);
                }
            }

            return particles;
        }

        void update(Particle particle, int frame) {
            if (particle.y >= 30.0) {
                particle.image = null;
            } else {
                int dt = frame - particle.t;
                if (dt < 0) {
                    particle.image = null;
                } else if (dt <= 9) {
                    particle.image = Animation.blood[9 - dt];
                } else {
                    particle.vy = particle.vy + 0.4 * Math.random();
                }

            }
        }
    }
}
