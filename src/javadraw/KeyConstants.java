package javadraw;

/**
 * Defines constants for key event types. Used by {@link Window#addKeyEventListener(String, KeyEventListener)}.
 */
public class KeyConstants {
    /**
     * Event type fired when a key is pressed. Events fired of this type are also passed to {@link Window#keyDown(Key)}.
     */
    public static final String KEY_PRESSED = "Press";
    /**
     * Event type fires when a key is held. Events fired of this type are not passed to a method.
     * @deprecated This value is only used internally and is not fired
     */
    public static final String KEY_HELD = "Hold";
    /**
     * Event type fired when a key is released. Events fired of this type are also passed to {@link Window#keyUp(Key)}.
     */
    public static final String KEY_RELEASED = "Release";
}
