//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import javadraw.KeyEventListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

public class KeyInterpreter implements KeyListener {
    private static HashMap<Integer, String> codeNames;
    private static HashMap<String, Integer> keyCodes = buildKeyCodes();
    private Object target;
    private HashSet keysDown = new HashSet();
    private Component canvas;

    private HashMap<String, javadraw.internal.KeyEvent> eventMap = new HashMap<>();
    {
        eventMap.put("Hold", new javadraw.internal.KeyEvent());
        eventMap.put("Press", new javadraw.internal.KeyEvent());
        eventMap.put("Release", new javadraw.internal.KeyEvent());
    }

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

            for(int i = 0; i < fields.length; ++i) {
                if ((fields[i].getModifiers() & 8) != 0 && fields[i].getName().startsWith("VK_")) {
                    String name = fields[i].getName().substring(3).toLowerCase();
                    Integer value = (Integer)fields[i].get(null);
                    keyCodes.put(name, value);
                    codeNames.put(value, name);
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
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
        Integer code = event.getKeyCode();
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
                    eventMap.get(type).fire(name);
                } catch (InvocationTargetException var21) {
                    var21.getCause().printStackTrace();
                } catch (Exception var22) {
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
                        var22.getCause().printStackTrace();
                    } catch (NoSuchMethodException var19) {
                    } catch (Exception var20) {
                        var22.printStackTrace();
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
                Class[] clazzes = new Class[]{String.class};
                String[] key = new String[]{"" + event.getKeyChar()};
                Method method = this.target.getClass().getMethod("onKeyTyped", clazzes);
                method.invoke(this.target, key);
            } catch (InvocationTargetException var9) {
                var9.getCause().printStackTrace();
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

    public void addKeyEventListener(String type, KeyEventListener listener) {
        eventMap.get(type).addListener(listener);
    }
}
