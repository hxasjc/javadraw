//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

public class KeyInterpreter implements KeyListener {
    private static HashMap<Integer, String> codeNames;
    private static final HashMap<String, Integer> keyCodes = buildKeyCodes();
    private Object target;
    private final HashSet<Integer> keysDown = new HashSet<>();
    private Component canvas;

    public KeyInterpreter(Object target, DrawingCanvas canvas) {
        this.canvas = (Component)canvas;
        this.canvas.addKeyListener(this);
        this.target = target;
    }

    public void removeFromCanvas() {
        if (this.canvas != null) {
            this.canvas.removeKeyListener(this);
            this.canvas = null;
        }

    }

    public void addToCanvas(DrawingCanvas canvas) {
        if (canvas != this.canvas) {
            this.removeFromCanvas();
            this.canvas = (Component)canvas;
            this.canvas.addKeyListener(this);
        }

    }

    public DrawingCanvas getCanvas() {
        return (DrawingCanvas)this.canvas;
    }

    private static HashMap<String, Integer> buildKeyCodes() {
        HashMap<String, Integer> keyCodes = new HashMap<>();
        codeNames = new HashMap<>();

        try {
            Field[] fields = KeyEvent.class.getFields();

            for (Field field : fields) {
                if ((field.getModifiers() & 8) != 0 && field.getName().startsWith("VK_")) {
                    String name = field.getName().substring(3).toLowerCase();
                    Integer value = (Integer) field.get(null);
                    keyCodes.put(name, value);
                    codeNames.put(value, name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyCodes;
    }

    private static Integer codeForKeyName(String key) {
        return (Integer)keyCodes.get(key.toLowerCase());
    }

    private static String nameForKeyCode(Integer code) {
        return (String)codeNames.get(code);
    }

    private static String uppercaseNameForKeyCode(Integer code) {
        return ((String)codeNames.get(code)).toUpperCase();
    }

    private static String capitalizedNameForKeyCode(Integer code) {
        String s = (String)codeNames.get(code);
        return s.substring(0, 1).toUpperCase().concat(s.substring(1));
    }

    private void keyEvent(KeyEvent event) {
        int code = event.getKeyCode();
        if (this.target != null) {
            String type;
            if (event.getID() == 401) {
                if (this.keysDown.contains(code)) {
                    type = "Hold";
                } else {
                    type = "Press";
                    this.keysDown.add(code);
                }
            } else {
                if (event.getID() != 402) {
                    return;
                }

                type = "Release";
                this.keysDown.remove(code);
            }

            String name = capitalizedNameForKeyCode(code);
            if (name != null) {
                (new StringBuilder()).append("on").append(name).append(type).toString();
                String superMethodName = "onKey" + type;
                ObjectDrawObject.deferUpdates();

                try {
                    Method superMethod = this.target.getClass().getMethod(superMethodName, String.class);
                    superMethod.setAccessible(true);
                    superMethod.invoke(this.target, name);
                } catch (InvocationTargetException e) {
                    e.getCause().printStackTrace();
                } catch (Exception e) {
                    try {
                        Class[] c = new Class[]{String.class};
                        String[] key = new String[1];
                        if (event.isShiftDown()) {
                            key[0] = capitalizedNameForKeyCode(code);
                        } else {
                            key[0] = nameForKeyCode(code);
                        }

                        if (event.isAltDown()) {
                            key[0] = "~" + key[0];
                        }

                        if (event.isControlDown()) {
                            key[0] = "^" + key[0];
                        }

                        if (event.isMetaDown()) {
                            key[0] = "&" + key[0];
                        }

                        Method method = this.target.getClass().getMethod("onOther" + type, c);
                        method.invoke(this.target, key);
                    } catch (InvocationTargetException var18) {
                        e.getCause().printStackTrace();
                    } catch (NoSuchMethodException ignored) {
                    } catch (Exception var20) {
                        e.printStackTrace();
                    }
                } finally {
                    ObjectDrawObject.runUpdates();
                }

            }
        }
    }

    /** @deprecated */
    public void keyTyped(KeyEvent event) {
        if (this.target != null && !event.isActionKey() && event.getKeyChar() != '\uffff' && (event.getKeyChar() == '\n' || !Character.isISOControl(event.getKeyChar()))) {
            ObjectDrawShape.deferUpdates();

            try {
                Class[] c = new Class[]{String.class};
                String[] key = new String[]{"" + event.getKeyChar()};
                Method method = this.target.getClass().getMethod("onKeyTyped", c);
                method.invoke(this.target, key);
            } catch (InvocationTargetException e) {
                e.getCause().printStackTrace();
            } catch (Exception ignored) {
            } finally {
                ObjectDrawObject.runUpdates();
            }

        }
    }

    /** @deprecated */
    public void keyPressed(KeyEvent event) {
        this.keyEvent(event);
    }

    /** @deprecated */
    public void keyReleased(KeyEvent event) {
        this.keyEvent(event);
    }
}
