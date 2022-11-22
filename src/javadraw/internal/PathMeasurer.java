//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.HashMap;

class PathMeasurer {
    private static HashMap segments = new HashMap();
    private static final double ACCURACY = 0.1;

    PathMeasurer() {
    }

    private static Segment getSegment(int type) {
        return (Segment)segments.get(type);
    }

    static Point[] getPoints(Shape shape, double separation) {
        return (new StrokeData(shape.getPathIterator((AffineTransform)null))).measure(separation);
    }

    static {
        segments.put(4, new CloseSegment());
        segments.put(0, new MoveSegment());
        segments.put(1, new LineSegment());
        segments.put(2, new QuadraticSplineSegment());
        segments.put(3, new CubicSplineSegment());
    }

    private static class CubicSplineSegment extends SplineSegment {
        private CubicSplineSegment() {
            super();
        }

        protected void prepare(StrokeData data) {
            data.linearX = 3.0 * (data.coordinates[0] - data.currentX);
            data.linearY = 3.0 * (data.coordinates[1] - data.currentY);
            data.quadraticX = 3.0 * data.currentX - 6.0 * data.coordinates[0] + 3.0 * data.coordinates[2];
            data.quadraticY = 3.0 * data.currentY - 6.0 * data.coordinates[1] + 3.0 * data.coordinates[3];
            data.cubicX = -data.currentX + 3.0 * data.coordinates[0] - 3.0 * data.coordinates[2] + data.coordinates[4];
            data.cubicY = -data.currentY + 3.0 * data.coordinates[1] - 3.0 * data.coordinates[3] + data.coordinates[5];
        }

        protected double getEndX(StrokeData data) {
            return data.coordinates[4];
        }

        protected double getEndY(StrokeData data) {
            return data.coordinates[5];
        }

        protected double getX(StrokeData data, double t) {
            return data.currentX + t * (data.linearX + t * (data.quadraticX + t * data.cubicX));
        }

        protected double getY(StrokeData data, double t) {
            return data.currentY + t * (data.linearY + t * (data.quadraticY + t * data.cubicY));
        }

        protected Point getPoint(StrokeData data, double t) {
            double dx = data.linearX + t * (2.0 * data.quadraticX + 3.0 * t * data.cubicX);
            double dy = data.linearY + t * (2.0 * data.quadraticY + 3.0 * t * data.cubicY);
            double d = Math.sqrt(dx * dx + dy * dy);
            dx /= d;
            dy /= d;
            return new Point(data.currentX + t * (data.linearX + t * (data.quadraticX + t * data.cubicX)), data.currentY + t * (data.linearY + t * (data.quadraticY + t * data.cubicY)), dy, -dx);
        }
    }

    private static class QuadraticSplineSegment extends SplineSegment {
        private QuadraticSplineSegment() {
            super();
        }

        protected void prepare(StrokeData data) {
            data.linearX = 2.0 * (data.coordinates[0] - data.currentX);
            data.linearY = 2.0 * (data.coordinates[1] - data.currentY);
            data.quadraticX = data.currentX - 2.0 * data.coordinates[0] + data.coordinates[2];
            data.quadraticY = data.currentY - 2.0 * data.coordinates[1] + data.coordinates[3];
        }

        protected double getEndX(StrokeData data) {
            return data.coordinates[2];
        }

        protected double getEndY(StrokeData data) {
            return data.coordinates[3];
        }

        protected double getX(StrokeData data, double t) {
            return data.currentX + t * (data.linearX + t * data.quadraticX);
        }

        protected double getY(StrokeData data, double t) {
            return data.currentY + t * (data.linearY + t * data.quadraticY);
        }

        protected Point getPoint(StrokeData data, double t) {
            double dx = data.linearX + 2.0 * t * data.quadraticX;
            double dy = data.linearY + 2.0 * t * data.quadraticY;
            double d = Math.sqrt(dx * dx + dy * dy);
            dx /= d;
            dy /= d;
            return new Point(data.currentX + t * (data.linearX + t * data.quadraticX), data.currentY + t * (data.linearY + t * data.quadraticY), dy, -dx);
        }
    }

    private abstract static class SplineSegment extends Segment {
        private SplineSegment() {
            super();
        }

        protected abstract double getEndX(StrokeData var1);

        protected abstract double getEndY(StrokeData var1);

        protected abstract double getX(StrokeData var1, double var2);

        protected abstract double getY(StrokeData var1, double var2);

        protected abstract Point getPoint(StrokeData var1, double var2);

        protected abstract void prepare(StrokeData var1);

        public void measure(ArrayList points, StrokeData data, double sep) {
            this.prepare(data);
            double t = 0.0;
            double x = data.currentX;
            double y = data.currentY;
            double error = sep * sep * 0.1 * 0.1;
            double[] stack = new double[300];
            int top = 0;
            stack[0] = 1.0;
            stack[1] = this.getEndX(data);
            stack[2] = this.getEndY(data);

            for(int i = 1; i < 4; ++i) {
                top = i * 3;
                stack[top] = 1.0 - 0.25 * (double)i;
                stack[top + 1] = this.getX(data, stack[top]);
                stack[top + 2] = this.getY(data, stack[top]);
            }

            while(top >= 0) {
                double dx = stack[top + 1] - x;
                double dy = stack[top + 2] - y;
                double length = dx * dx + dy * dy;
                double tt;
                if (length < error) {
                    length = Math.sqrt(length);
                    if (length > data.currentOffset) {
                        tt = ((length - data.currentOffset) * t + data.currentOffset * stack[top]) / length;
                        points.add(this.getPoint(data, tt));
                        data.currentOffset = data.currentOffset + sep;
                    }

                    data.currentOffset = data.currentOffset - length;
                    t = stack[top];
                    x = stack[top + 1];
                    y = stack[top + 2];
                    top -= 3;
                } else {
                    tt = (stack[top] + t) / 2.0;
                    top += 3;
                    stack[top] = tt;
                    stack[top + 1] = this.getX(data, tt);
                    stack[top + 2] = this.getY(data, tt);
                }
            }

            data.currentX = this.getEndX(data);
            data.currentY = this.getEndY(data);
        }
    }

    private static class CloseSegment extends LineSegment {
        private CloseSegment() {
            super();
        }

        protected double getEndX(StrokeData data) {
            return data.startX;
        }

        protected double getEndY(StrokeData data) {
            return data.startY;
        }
    }

    private static class MoveSegment extends LineSegment {
        private MoveSegment() {
            super();
        }

        public void measure(ArrayList points, StrokeData data, double separation) {
            data.currentX = data.startX = this.getEndX(data);
            data.currentY = data.startY = this.getEndY(data);
        }
    }

    private static class LineSegment extends Segment {
        private LineSegment() {
            super();
        }

        private double length(StrokeData data) {
            data.linearX = this.getEndX(data) - data.currentX;
            data.linearY = this.getEndY(data) - data.currentY;
            return Math.sqrt(data.linearX * data.linearX + data.linearY * data.linearY);
        }

        protected double getEndX(StrokeData data) {
            return data.coordinates[0];
        }

        protected double getEndY(StrokeData data) {
            return data.coordinates[1];
        }

        public void measure(ArrayList points, StrokeData data, double separation) {
            double length = this.length(data);
            if (length <= data.currentOffset) {
                data.currentX = this.getEndX(data);
                data.currentY = this.getEndY(data);
                data.currentOffset = data.currentOffset - length;
            } else {
                double dt = separation / length;
                double t = data.currentOffset / length;
                double dx = data.linearX / length;

                for(double dy = data.linearY / length; t <= 1.0; t += dt) {
                    points.add(new Point(data.currentX + t * data.linearX, data.currentY + t * data.linearY, dy, -dx));
                }

                data.currentOffset = (t - 1.0) * length;
                data.currentX = this.getEndX(data);
                data.currentY = this.getEndY(data);
            }
        }
    }

    private abstract static class Segment {
        private Segment() {
        }

        public abstract void measure(ArrayList var1, StrokeData var2, double var3);
    }

    private static class StrokeData {
        private PathIterator path;
        private double startX;
        private double startY;
        private double currentX;
        private double currentY;
        private double currentOffset;
        private double[] coordinates = new double[6];
        private double linearX;
        private double linearY;
        private double quadraticX;
        private double quadraticY;
        private double cubicX;
        private double cubicY;

        public StrokeData(PathIterator path) {
            this.path = path;
        }

        public Point[] measure(double separation) {
            this.startX = this.startY = this.currentX = this.currentY = this.currentOffset = 0.0;

            ArrayList points;
            for(points = new ArrayList(); !this.path.isDone(); this.path.next()) {
                Segment seg = PathMeasurer.getSegment(this.path.currentSegment(this.coordinates));
                if (seg != null) {
                    seg.measure(points, this, separation);
                }
            }

            return (Point[])((Point[])points.toArray(new Point[points.size()]));
        }
    }

    public static class Point {
        public final double x;
        public final double y;
        public final double nx;
        public final double ny;

        private Point(double x, double y, double nx, double ny) {
            this.x = x;
            this.y = y;
            this.nx = nx;
            this.ny = ny;
        }
    }
}
