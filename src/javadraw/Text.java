package javadraw;

import javadraw.internal.SneakyThrow;

/**
 * Class to represent text in javaDraw. Note that underline and strikethrough are not currently implemented.
 */
public class Text extends Renderable {

    private final Object[] parameterValues;

    public Text(Screen screen, Object text, Location location, Color color, Color border, String font, int size, String align,
                boolean bold, boolean italic, boolean underline, boolean strikethrough, double rotation, boolean visible) {
        // Just initialize the variables for us :)
        super(screen, location, -1, -1, color, Color.NONE, false, rotation, visible);
        this.parameterValues = new Object[] {screen, text, location, color, border, font, size, align, bold, italic, underline, strikethrough, rotation, visible};

        this.object = new javadraw.internal.Text(text, location.location, screen.canvas);

        if(font == null || font.equalsIgnoreCase("")) {
            ((javadraw.internal.Text) this.object).setFont(javadraw.internal.Text.DEFAULT_FONT);
        } else {
            ((javadraw.internal.Text) this.object).setFont(font);
        }

        ((javadraw.internal.Text) this.object).setFontSize(size);

        double alignment = javadraw.internal.Text.LEFT;

        // I don't know why, and I don't want to know why, but we have to do it all this way; I know it's weird.

        if (align.equalsIgnoreCase("left")) {
            alignment = javadraw.internal.Text.LEFT;
            // ((javadraw.internal.Text) this.object).setAlignment(alignment, javadraw.internal.Text.TOP);
        } else if(align.equalsIgnoreCase("center")) {
            alignment = javadraw.internal.Text.CENTER;
            // ((javadraw.internal.Text) this.object).setAlignment(alignment, javadraw.internal.Text.TOP);
        } else if(align.equalsIgnoreCase("right")) {
            alignment = javadraw.internal.Text.RIGHT;
            // ((javadraw.internal.Text) this.object).setAlignment(alignment, javadraw.internal.Text.TOP);
        } else {
            // System.out.println(align);
            SneakyThrow.sneakyThrow(new Exception("Alignment passed was not 'LEFT', 'CENTER', or 'RIGHT'. Found: " + align));
        }

        ((javadraw.internal.Text) this.object).setAlignment(alignment, javadraw.internal.Text.TOP);

        ((javadraw.internal.Text) this.object).setBold(bold);
        ((javadraw.internal.Text) this.object).setItalic(italic);
        // TODO: Work out how to get underline and strikethrough into all this.

        this.object.setColor(color.toAWT());

        // Let's make sure we still add the width and height after we create the Text.
        this.width = this.object.getDoubleWidth();
        this.height = this.object.getDoubleHeight();
    }

    public Text(Screen screen, Object text, double x, double y, Color color, Color border, String font, int size, String align,
                boolean bold, boolean italic, boolean underline, boolean strikethrough, double rotation, boolean visible)  {
        this(screen, text, new Location(x, y), color, border, font, size, align, bold, italic, underline, strikethrough, rotation, visible);
    }

    public Text(Screen screen, Object text, double x, double y, Color color) {
        this(screen, text, x, y, color, Color.NONE, "", 12, "LEFT", false, false, false, false, 0, true);
    }

    public Text(Screen screen, Object text, double x, double y) {
        this(screen, text, x, y, Color.BLACK);
    }

    public Text(Screen screen, Object text, double x, double y, double rotation) {
        this(screen, text, x, y, Color.BLACK, Color.NONE, "", 12, "LEFT", false, false, false, false, rotation, true);
    }

    public Text(Screen screen, Object text, Location location, Color color) {
        this(screen, text, location.x(), location.y(), color);
    }

    public Text(Screen screen, Object text, Location location, double rotation) {
        this(screen, text, location.x(), location.y(), rotation);
    }

    public Text(Screen screen, Object text, Location location) {
        this(screen, text, location.x(), location.y());
    }

    /**
     * Get the String displayed by the Text
     * @return what's displayed by the Text
     */
    public String text() {
        return ((javadraw.internal.Text) this.object).getText();
    }

    /**
     * Set the String displayed by the Text
     * @param text the new String to display
     * @return the new String passed
     */
    public String text(String text) {
        ((javadraw.internal.Text) this.object).setText(text);

        this.update();
        return text;
    }

    /**
     * Get the font displayed by the Text
     * @return a String representing the name of the Font
     */
    public String font() {
        return ((javadraw.internal.Text) this.object).getFont().getName();
    }

    /**
     * Set the font displayed by the Text
     * @param font the new font to set
     * @return the new font passed
     */
    public String font(String font) {
        ((javadraw.internal.Text) this.object).setFont(font);

        this.update();
        return this.font();
    }

    /**
     * Get the font-size of the Text displayed
     * @return the font size (as an integer)
     */
    public int size() {
        return ((javadraw.internal.Text) this.object).getFont().getSize();
    }

    /**
     * Set the font-size of the Text displayed
     * @param size the new font-size to set to
     * @return the new font-size
     */
    public int size(int size) {
        ((javadraw.internal.Text) this.object).setFontSize(size);

        this.update();
        return this.size();
    }

    /**
     * Get the alignment of the Text displayed
     * @return the alignment, as a String. Could be: "LEFT", "CENTER", or "RIGHT".
     */
    public String align() {
        double alignment = ((javadraw.internal.Text) this.object).getHorizontalAlignment();
        if(alignment == javadraw.internal.Text.LEFT) {
            return "LEFT";
        } else if(alignment == javadraw.internal.Text.CENTER) {
            return "CENTER";
        } else {
            return "RIGHT";
        }
    }

    /**
     * Set the alignment of the Text displayed
     * @param align the new alignment to set to (either "LEFT", "CENTER", or "RIGHT", case insensitive).
     * @return the new alignment
     */
    public String align(String align) {
        double alignment = javadraw.internal.Text.LEFT;

        if (align.equalsIgnoreCase("left")) {
            alignment = javadraw.internal.Text.LEFT;
        } else if(align.equalsIgnoreCase("center")) {
            alignment = javadraw.internal.Text.CENTER;
        } else if(align.equalsIgnoreCase("right")) {
            alignment = javadraw.internal.Text.RIGHT;
        } else {
            SneakyThrow.sneakyThrow(new IllegalArgumentException("Alignment passed was not 'LEFT', 'CENTER', or 'RIGHT'. Found: " + align));
        }

        ((javadraw.internal.Text) this.object).setAlignment(alignment, ((javadraw.internal.Text) this.object).getVerticalAlignment());

        this.update();
        return align.toUpperCase();
    }

    /**
     * Get whether the Text displayed is bold or not
     * @return the Text's bold-state
     */
    public boolean bold() {
        return ((javadraw.internal.Text) this.object).getFont().isBold();
    }

    /**
     * Set the bold-state of the Text displayed
     * @param bold the new bold-state to be set
     * @return the new bold state
     */
    public boolean bold(boolean bold) {
        ((javadraw.internal.Text) this.object).setBold(bold);
        this.update();
        return bold;
    }

    /**
     * Get whether the Text displayed is italic or not
     * @return the Text's italic-state
     */
    public boolean italic() {
        return ((javadraw.internal.Text) this.object).getFont().isItalic();
    }

    /**
     * Set the italic-state of the Text displayed
     * @param italic the new italic-state to be set
     * @return the new italic state
     */
    public boolean italic(boolean italic) {
        ((javadraw.internal.Text) this.object).setItalic(italic);
        this.update();
        return italic;
    }

    /**
     * Meant to update the width and height after changes to the font.
     */
    private void update() {
        try {
            this.width = this.object.getDoubleWidth();
            this.height = this.object.getDoubleHeight();
        } catch(NullPointerException e) {
            // Nothing.
        }
    }

    @Override
    protected Object[] getParameters() {
        return this.parameterValues;
    }
}
