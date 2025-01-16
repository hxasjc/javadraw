//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;

/** @deprecated */
public class TextWrappingLayout {
    private TextRun start;
    private double alignV;
    private double alignH;
    private Shape shape;
    private FontRenderContext context;
    private double endPadding = (double)4.0F;
    private static final double MIN_ERROR = (double)0.5F;
    private static final int WORKING = 0;
    private static final int GROW = 1;
    private static final int TEXT_END = 2;
    private static final int LINE_END = 3;
    public static final double ALIGN_TOP = (double)-1.0F;
    public static final double ALIGN_BOTTOM = (double)1.0F;
    public static final double ALIGN_LEFT = (double)-1.0F;
    public static final double ALIGN_RIGHT = (double)1.0F;
    public static final double ALIGN_CENTER = (double)0.0F;

    public TextWrappingLayout(TextRun start, double alignH, double alignV, Shape shape, FontRenderContext frc) {
        this.start = start;
        this.alignV = alignV;
        this.alignH = alignH;
        this.shape = shape;
        this.context = frc;
    }

    public void setEndPadding(double padding) {
        this.endPadding = padding;
    }

    private boolean doLayoutOnArc() {
        TextRun run = this.start;

        String text;
        for(text = ""; run != null; run = run.getNextRun()) {
            text = text + run.getText();
        }

        if (text.isEmpty()) {
            this.writeLayout(null);
            return true;
        } else {
            AttributedString string = new AttributedString(text);
            int offset = 0;

            for(TextRun var13 = this.start; var13 != null; var13 = var13.getNextRun()) {
                string.addAttribute(TextAttribute.FONT, var13.getFont(), offset, offset + var13.getText().length());
                offset += var13.getText().length();
            }

            String[] lines = text.split("\n");
            int end = text.length();
            int start = end;
            double height = (double)0.0F;
            TextLayout layout = null;

            for(int j = lines.length - 1; j >= 0; --j) {
                String line = lines[j];
                end = start;

                for(start -= line.length(); start < end && Character.isWhitespace(text.charAt(start)); ++start) {
                }

                while(start < end && Character.isWhitespace(text.charAt(end))) {
                    --end;
                }

                if (start != end) {
                    layout = new TextLayout(string.getIterator((AttributedCharacterIterator.Attribute[])null, start, end), this.context);
                    height += (double)(layout.getAscent() + layout.getDescent() + layout.getLeading());
                }
            }

            double var10000 = height - (double)layout.getLeading();
            this.writeLayout(null);
            return true;
        }
    }

    private boolean doLayoutNoShape() {
        TextRun run = this.start;

        String text;
        for(text = ""; run != null; run = run.getNextRun()) {
            text = text + run.getText();
        }

        if (text.isEmpty()) {
            this.writeLayout(null);
            return true;
        } else {
            AttributedString string = new AttributedString(text);
            int offset = 0;

            for(TextRun var18 = this.start; var18 != null; var18 = var18.getNextRun()) {
                int len = var18.getText().length();
                if (len > 0) {
                    string.addAttribute(TextAttribute.FONT, var18.getFont(), offset, offset + len);
                    offset += len;
                }
            }

            GeneralPath path = new GeneralPath();
            ArrayList<GeneralPath> paths = new ArrayList<>();
            run = this.start;

            int runEndOffset;
            for(runEndOffset = run.getText().length(); runEndOffset == 0; runEndOffset = run.getText().length()) {
                run = run.getNextRun();
            }

            offset = 0;
            double top = (double)0.0F;
            double spacing = (double)0.0F;

            TextLayout layout;
            for(layout = null; run != null; spacing = (double)((layout.getAscent() + layout.getDescent()) / 2.0F + layout.getLeading())) {
                char c;
                for(; offset < text.length() && Character.isWhitespace(c = text.charAt(offset)); ++offset) {
                    if (c == '\n') {
                        top += spacing;
                    }
                }

                if (offset == text.length()) {
                    paths.add(path);
                    break;
                }

                int endOffset = text.indexOf("\n", offset);
                if (endOffset == -1) {
                    endOffset = text.length();
                } else {
                    ++endOffset;
                }

                layout = new TextLayout(string.getIterator((AttributedCharacterIterator.Attribute[])null, offset, endOffset), this.context);
                AffineTransform transform = AffineTransform.getTranslateInstance(-layout.getBounds().getMinX() - ((double)1.0F + this.alignH) * layout.getBounds().getWidth() / (double)2.0F, top + (double)layout.getAscent());

                while(offset < endOffset) {
                    int end = Math.min(runEndOffset, endOffset);
                    if (offset != end) {
                        TextLayout runLayout = new TextLayout(string.getIterator((AttributedCharacterIterator.Attribute[])null, offset, end), this.context);
                        path.append(runLayout.getOutline(transform), false);
                        transform.translate((double)runLayout.getAdvance(), (double)0.0F);
                        offset = end;
                    }

                    if (offset == runEndOffset) {
                        run = run.getNextRun();
                        paths.add(path);
                        if (run != null) {
                            runEndOffset += run.getText().length();
                            path = new GeneralPath();
                        }
                    }
                }

                top += (double)(layout.getAscent() + layout.getDescent() + layout.getLeading());
            }

            top -= (double)layout.getLeading();
            AffineTransform transform = AffineTransform.getTranslateInstance((double)0.0F, -((double)1.0F + this.alignV) * top / (double)2.0F);
            run = this.start;

            for(int var22 = 0; run != null; ++var22) {
                run.setShape(transform.createTransformedShape((Shape)paths.get(var22)));
                run = run.getNextRun();
            }

            return true;
        }
    }

    private boolean layoutInRect(Rectangle2D rect) {
        HashMap<String, Double> map = this.tryLayout(rect.getMinY() + this.endPadding);
        double top = this.getMapTop(map);
        double bottom = this.getMapBottom(map);
        double height = bottom - top;
        double wiggleRoom = rect.getHeight() - (double)2.0F * this.endPadding - height;
        AffineTransform transform = AffineTransform.getTranslateInstance((double)0.0F, ((double)1.0F + this.alignV) * wiggleRoom / (double)2.0F);
        this.writeLayout(map, transform);
        return this.isFullLayout(map);
    }

    public boolean doLayout() {
        WordInfo info = this.getFirstWord(this.start, 0);
        if (!info.exists) {
            this.writeLayout(null);
            return true;
        } else if (this.shape == null) {
            return this.doLayoutNoShape();
        } else if (this.shape instanceof Arc2D && ((Arc2D)this.shape).getArcType() == Arc2D.OPEN) {
            return this.doLayoutOnArc();
        } else if (this.shape instanceof Rectangle2D) {
            return this.layoutInRect((Rectangle2D)this.shape);
        } else {
            Rectangle2D firstLine = this.placeWord(info.width, info.height, (double)-1.0F);
            if (firstLine == null) {
                this.writeLayout(null);
                return false;
            } else {
                double minY = firstLine.getMinY();
                HashMap<String, Double> map = this.tryLayout(minY);
                if (this.alignV != (double)-1.0F && this.isFullLayout(map)) {
                    double top = this.shape.getBounds2D().getMinY();
                    double bottom = this.shape.getBounds2D().getMaxY();
                    double maxY = this.placeWord(info.width, info.height, (double)1.0F).getMinY();
                    double align = ((double)1.0F + this.alignV) / (double)2.0F;
                    HashMap<String, Double> bestMap = map;
                    double bestAlign = (this.getMapTop(map) - top) / (this.getMapTop(map) - top + bottom - this.getMapBottom(map));
                    if (bestAlign > align) {
                        this.writeLayout(map);
                        return true;
                    } else {
                        boolean improved = true;

                        while(improved) {
                            improved = false;

                            for(double jump = maxY - minY; !improved && jump > (double)2.0F; jump /= (double)2.0F) {
                                for(double y = minY + jump / (double)2.0F; !improved && y < maxY; y += jump) {
                                    map = this.tryLayout(y);
                                    if (this.isFullLayout(map)) {
                                        double actualAlign = (this.getMapTop(map) - top) / (this.getMapTop(map) - top + bottom - this.getMapBottom(map));
                                        if (Math.abs(actualAlign - align) < Math.abs(bestAlign - align)) {
                                            bestAlign = actualAlign;
                                            bestMap = map;
                                            if (actualAlign > align) {
                                                maxY = y;
                                            } else {
                                                minY = y;
                                            }

                                            improved = true;
                                        }
                                    }
                                }
                            }
                        }

                        this.writeLayout(bestMap);
                        return true;
                    }
                } else {
                    this.writeLayout(map);
                    return this.isFullLayout(map);
                }
            }
        }
    }

    private WordInfo getFirstWord(TextRun run, int offset) {
        WordInfo info = new WordInfo();

        String text;
        for(text = ""; run != null; offset = 0) {
            for(text = run.getText(); offset < text.length() && Character.isWhitespace(text.charAt(offset)); ++offset) {
                if (text.charAt(offset) == '\n') {
                    ++info.newlines;
                }
            }

            info.run = run;
            info.offset = offset;
            info.totalOffset += offset;
            if (offset != text.length()) {
                break;
            }

            run = run.getNextRun();
        }

        if (run == null) {
            info.exists = false;
            return info;
        } else {
            int i;
            for(i = offset; i < text.length() && !Character.isWhitespace(text.charAt(i)); ++i) {
            }

            text = text.substring(offset, i);
            AttributedString astr = new AttributedString(text);
            astr.addAttribute(TextAttribute.FONT, run.getFont());
            TextLayout layout = new TextLayout(astr.getIterator(), this.context);
            info.width = layout.getBounds().getWidth() + (double)2.0F * this.endPadding + 0.1;
            info.height = (double)(layout.getAscent() + layout.getDescent());
            return info;
        }
    }

    private void expand(Rectangle2D rect, double minX, double maxX) {
        double y = rect.getMinY();
        double h = rect.getHeight();
        double x = rect.getMinX();
        double endX = rect.getMaxX();

        while(x - minX > (double)0.5F) {
            double mid = (x + minX) / (double)2.0F;
            rect.setRect(mid, y, endX - mid, h);
            if (this.shape.contains(rect)) {
                x = mid;
            } else {
                minX = mid;
            }
        }

        endX = x;
        x = rect.getMaxX();

        while(maxX - x > (double)0.5F) {
            double mid = (x + maxX) / (double)2.0F;
            rect.setRect(endX, y, mid - endX, h);
            if (this.shape.contains(rect)) {
                x = mid;
            } else {
                maxX = mid;
            }
        }

        rect.setRect(endX, y, x - endX, h);
    }

    private ArrayList<Rectangle2D> getLine(Rectangle2D line) {
        Rectangle2D rect = new Rectangle2D.Double();
        double minWidth = line.getHeight() * 0.2 + (double)2.0F * this.endPadding;

        for(double jump = line.getWidth(); jump > minWidth; jump /= (double)2.0F) {
            for(double x = jump / (double)2.0F; x <= line.getWidth(); x += jump) {
                rect.setRect(x + line.getMinX(), line.getMinY(), minWidth, line.getHeight());
                if (this.shape.contains(rect)) {
                    this.expand(rect, line.getMinX(), line.getMaxX());
                    ArrayList<Rectangle2D> list = this.getLine(new Rectangle2D.Double(line.getMinX(), line.getMinY(), rect.getMinX() - line.getMinX(), line.getHeight()));
                    list.add(rect);
                    list.addAll(this.getLine(new Rectangle2D.Double(rect.getMaxX(), line.getMinY(), line.getMaxX() - rect.getMaxX(), line.getHeight())));
                    return list;
                }
            }
        }

        return new ArrayList<>();
    }

    private boolean lineHasSpace(Rectangle2D line, double width) {
        ArrayList<Rectangle2D> rects = this.getLine(line);

        for(int i = 0; i < rects.size(); ++i) {
            Rectangle2D rect = rects.get(i);
            if (rect.getWidth() >= width) {
                return true;
            }
        }

        return false;
    }

    private Rectangle2D placeWord(double width, double height, double align) {
        double minY = this.shape.getBounds2D().getMinY() + this.endPadding;
        double maxY = this.shape.getBounds2D().getMaxY() - height - this.endPadding;
        align = ((double)1.0F + align) / (double)2.0F;
        double bestY = minY * ((double)1.0F - align) + maxY * align;
        double left = this.shape.getBounds2D().getMinX();
        double lineWidth = this.shape.getBounds2D().getWidth();
        Rectangle2D bestLine = null;
        Rectangle2D line = new Rectangle2D.Double();
        boolean improved = true;

        while(improved) {
            improved = false;

            for(double jump = maxY - minY; !improved && jump > (double)0.5F; jump /= (double)2.0F) {
                for(double y = minY + jump / (double)2.0F; !improved && y < maxY; y += jump) {
                    line.setRect(left, y, lineWidth, height);
                    if (this.lineHasSpace(line, width)) {
                        bestLine = line;
                        line = new Rectangle2D.Double();
                        double off = Math.abs(y - bestY);
                        minY = Math.max(minY, bestY - off);
                        maxY = Math.min(maxY, bestY + off);
                        improved = true;
                    }
                }
            }
        }

        return bestLine;
    }

    private LineLayout layoutLine(TextRun run, int offset, double top) {
        WordInfo first = this.getFirstWord(run, offset);
        run = first.run;
        offset = first.offset;

        LineLayout oldLine;
        LineLayout newLine;
        for(oldLine = new LineLayout(run, offset, first.height, top); oldLine.getStatus() == 1; oldLine = newLine) {
            newLine = new LineLayout(run, offset, oldLine.getGrowHeight(), top);
            if (newLine.getTotalOffset() < oldLine.getTotalOffset()) {
                return oldLine;
            }
        }

        return oldLine;
    }

    private HashMap<String, Double> tryLayout(double minY) {
        double maxY = this.shape.getBounds2D().getMaxY() - this.endPadding;
        HashMap<String, Double> map = new HashMap<>();
        map.put("Top", minY);
        int offset = 0;
        int totalChars = 0;
        TextRun run = this.start;

        double bottom;
        LineLayout line;
        for(bottom = minY; minY < maxY; totalChars += line.getTotalOffset()) {
            line = this.layoutLine(run, offset, minY);
            if (line.getLastRun() != run || line.getEndOffset() != offset) {
                bottom = line.getBottom();
            }

            line.writeToMap(map);
            if (line.getStatus() == 2) {
                map.put("Bottom", bottom);
                return map;
            }

            offset = line.getEndOffset();
            minY = line.getBottom();
            run = line.getLastRun();
        }

        map.put("Bottom", bottom);
        map.put("Count", (double) totalChars);
        return map;
    }

    private double getMapBottom(HashMap<String, Double> map) {
        Double b = map.get("Bottom");
        return b == null ? Double.NaN : b;
    }

    private double getMapTop(HashMap<String, Double> map) {
        return (Double)map.get("Top");
    }

    private int getMapLength(HashMap<String, Double> map) {
        return map.get("Count").intValue();
    }

    private boolean isFullLayout(HashMap<String, Double> map) {
        return map.get("Count") == null;
    }

    private void writeLayout(HashMap<String, Double> map) {
        this.writeLayout(map, null);
    }

    private void writeLayout(HashMap map, AffineTransform transform) {
        for(TextRun run = this.start; run != null; run = run.getNextRun()) {
            Shape path;
            if (map != null && (path = (GeneralPath)map.get(run)) != null) {
                if (transform != null) {
                    path = transform.createTransformedShape(path);
                }
            } else {
                path = new GeneralPath();
            }

            run.setShape(path);
        }

    }

    /** @deprecated */
    public static class SimpleTextRun implements TextRun {
        private Font font;
        private Color color;
        private String text;
        private SimpleTextRun next;
        private Shape shape;

        public SimpleTextRun(String text, Font font, Color color) {
            this.text = text;
            this.font = font;
            this.color = color;
        }

        public Font getFont() {
            return this.font;
        }

        public TextRun getNextRun() {
            return this.next;
        }

        public String getText() {
            return this.text;
        }

        public void setShape(Shape shape) {
            this.shape = shape;
        }

        public void append(SimpleTextRun run) {
            if (this.next == null) {
                this.next = run;
            } else {
                this.next.append(run);
            }

        }

        public void prepareLayout(Shape shape, double alignH, double alignV, FontRenderContext frc) {
            (new TextWrappingLayout(this, alignH, alignV, shape, frc)).doLayout();
        }

        public void draw(Graphics2D g) {
            if (this.shape != null) {
                g.setColor(this.color);
                g.fill(this.shape);
            }

            if (this.next != null) {
                this.next.draw(g);
            }

        }
    }

    private static class WordInfo {
        public int newlines;
        public int offset;
        public int totalOffset;
        public TextRun run;
        public double width;
        public double height;
        public boolean exists;

        private WordInfo() {
            this.newlines = 0;
            this.totalOffset = 0;
            this.exists = true;
        }
    }

    private class LineLayout {
        private TextRun firstRun;
        private TextRun lastRun;
        private int totalOffset;
        private int endOffset;
        private ArrayList<GeneralPath> shapes;
        private int status;
        private double growHeight;
        private double bottom;

        public LineLayout(TextRun firstRun, int startOffset, double height, double top) {
            this.firstRun = firstRun;
            this.shapes = new ArrayList<>();
            this.bottom = top + height;
            this.doLayout(startOffset, height, top);
        }

        public void doLayout(int offset, double height, double top) {
            TextRun run = this.firstRun;

            String text;
            int limit;
            for(text = run.getText().substring(offset); (limit = text.indexOf("\n")) == -1 && (run = run.getNextRun()) != null; text = text + run.getText()) {
            }

            if (limit < 0) {
                limit = text.length();
            }

            if (limit == 0) {
                this.status = 2;
            } else {
                AttributedString string = new AttributedString(text);
                run = this.firstRun;
                int endOfRun = run.getText().length() - offset;
                int pos = endOfRun;
                string.addAttribute(TextAttribute.FONT, run.getFont(), 0, endOfRun);

                while((run = run.getNextRun()) != null) {
                    int len = run.getText().length();
                    if (len > 0) {
                        string.addAttribute(TextAttribute.FONT, run.getFont(), pos, pos + len);
                        pos += len;
                    }
                }

                LineBreakMeasurer measurer = new LineBreakMeasurer(string.getIterator(), TextWrappingLayout.this.context);
                GeneralPath path = new GeneralPath();
                this.shapes.add(path);
                double align = ((double)1.0F + TextWrappingLayout.this.alignH) / (double)2.0F;
                run = this.firstRun;
                this.lastRun = this.firstRun;
                this.status = 0;
                double leading = 0.0F;
                ArrayList<Rectangle2D> rects = TextWrappingLayout.this.getLine(new Rectangle2D.Double(TextWrappingLayout.this.shape.getBounds2D().getMinX(), top, TextWrappingLayout.this.shape.getBounds2D().getWidth(), height));

                for(int i = 0; i < rects.size() && this.status == 0; ++i) {
                    Rectangle2D rect = (Rectangle2D)rects.get(i);
                    float layoutWidth = (float)(rect.getWidth() - (double)2.0F * TextWrappingLayout.this.endPadding);
                    pos = measurer.getPosition();
                    if (pos < limit && !((double)layoutWidth < 0.2 * height)) {
                        TextLayout layout = measurer.nextLayout(layoutWidth, limit, true);
                        if (layout != null && pos != measurer.getPosition()) {
                            if ((double)(layout.getAscent() + layout.getDescent()) > height) {
                                this.status = 1;
                                this.growHeight = (double)(layout.getAscent() + layout.getDescent());
                                int minPos = pos;
                                int maxPos = measurer.getPosition();

                                while(maxPos - minPos > 1) {
                                    int posLimit = (minPos + maxPos) / 2;
                                    measurer.setPosition(pos);
                                    layout = measurer.nextLayout(layoutWidth, posLimit, true);
                                    if ((double)(layout.getAscent() + layout.getDescent()) > height) {
                                        this.growHeight = (double)(layout.getAscent() + layout.getDescent());
                                        maxPos = posLimit;
                                    } else {
                                        minPos = posLimit;
                                    }
                                }

                                measurer.setPosition(pos);
                                layout = measurer.nextLayout(layoutWidth, minPos, true);
                            }

                            double width = layout.getBounds().getWidth();
                            leading = Math.max(leading, (double)layout.getLeading());
                            double padding;
                            if (rect.getWidth() - width < (double)2.0F * TextWrappingLayout.this.endPadding) {
                                padding = (rect.getWidth() - width) / (double)2.0F;
                            } else {
                                padding = TextWrappingLayout.this.endPadding;
                            }

                            double x = -layout.getBounds().getMinX() + ((double)1.0F - align) * (rect.getMinX() + padding) + align * (rect.getMaxX() - width - padding);
                            double y = rect.getMaxY() - (double)layout.getDescent();
                            AffineTransform transform = AffineTransform.getTranslateInstance(x, y);
                            this.totalOffset = measurer.getPosition();
                            if (this.totalOffset > endOfRun) {
                                measurer.setPosition(pos);
                                layout = measurer.nextLayout(layoutWidth, endOfRun, true);

                                while(measurer.getPosition() < this.totalOffset) {
                                    path.append(layout.getOutline(transform), false);
                                    run = run.getNextRun();
                                    this.lastRun = run;
                                    int len = run.getText().length();
                                    endOfRun += len;
                                    path = new GeneralPath();
                                    this.shapes.add(path);
                                    transform.translate((double)layout.getAdvance(), (double)0.0F);
                                    if (len > 0) {
                                        layout = measurer.nextLayout(layoutWidth, Math.min(endOfRun, this.totalOffset), true);
                                    } else {
                                        layout = new TextLayout(" ", run.getFont(), TextWrappingLayout.this.context);
                                    }
                                }
                            }

                            path.append(layout.getOutline(transform), false);
                            if (this.totalOffset >= limit) {
                                if (limit != text.length()) {
                                    this.status = 3;
                                    this.endOffset = this.totalOffset + this.lastRun.getText().length() - endOfRun;
                                    WordInfo next = TextWrappingLayout.this.getFirstWord(this.lastRun, this.endOffset);
                                    this.bottom += leading + (double)(next.newlines - 1) * (height / (double)2.0F + leading);
                                    this.lastRun = next.run;
                                    this.endOffset = next.offset;
                                    this.totalOffset += next.totalOffset;
                                    return;
                                }

                                this.status = 2;
                            }
                        }
                    }
                }

                if (this.status == 0) {
                    this.status = 3;
                }

                this.bottom += leading;
                this.endOffset = this.totalOffset + this.lastRun.getText().length() - endOfRun;
            }
        }

        public void writeToMap(HashMap map) {
            TextRun run = this.firstRun;

            for(int i = 0; i < this.shapes.size(); run = run.getNextRun()) {
                GeneralPath path = (GeneralPath)map.get(run);
                if (path == null) {
                    path = new GeneralPath();
                    map.put(run, path);
                }

                path.append((GeneralPath)this.shapes.get(i), false);
                ++i;
            }

        }

        public int getStatus() {
            return this.status;
        }

        public double getGrowHeight() {
            return this.growHeight;
        }

        public int getTotalOffset() {
            return this.totalOffset;
        }

        public int getEndOffset() {
            return this.endOffset;
        }

        public TextRun getLastRun() {
            return this.lastRun;
        }

        public double getBottom() {
            return this.bottom;
        }
    }

    /** @deprecated */
    public interface TextRun {
        void setShape(Shape var1);

        Font getFont();

        String getText();

        TextRun getNextRun();
    }
}
