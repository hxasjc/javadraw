//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.Timer;

public class EventTimer {
    public static final Object SELF = new Object();
    private static final Set activeTimers = Collections.synchronizedSet(new HashSet());
    private Timer timer;
    private Object[] args;
    private Object target;
    private Method method;
    private long lastFireTime = -1L;
    private long startTime = -1L;
    private long initialFireTime = -1L;
    private ActionListener listener;

    public static void stopAll() {
        synchronized(activeTimers) {
            Iterator iter = activeTimers.iterator();

            while(iter.hasNext()) {
                ((EventTimer)iter.next()).timer.stop();
            }

            activeTimers.clear();
        }
    }

    public EventTimer(Object target, String methodName, Object[] args) {
        class NamelessClass_1 implements ActionListener {
            NamelessClass_1() {
            }

            public void actionPerformed(ActionEvent e) {
                EventTimer.this.fire();
            }
        }

        this.listener = new NamelessClass_1();
        this.setTarget(target, methodName, args);
    }

    public void setTarget(Object target, String methodName, Object[] args) {
        Class[] types = new Class[args.length];

        for(int i = 0; i < args.length; ++i) {
            if (args[i] == SELF) {
                args[i] = this;
            }

            types[i] = args[i] == null ? null : args[i].getClass();
        }

        if (target == null) {
            throw new IllegalArgumentException("Timer target cannot be null.");
        } else {
            try {
                Class targetClass = target instanceof Class ? (Class)target : target.getClass();
                Method[] methods = targetClass.getMethods();

                for(int i = 0; i < methods.length; ++i) {
                    if (matches(methods[i], methodName, types)) {
                        this.method = methods[i];
                        this.target = target;
                        this.args = args;
                        this.timer = new Timer(1000, this.listener);
                        checkTypes(args, this.method);
                        break;
                    }
                }
            } catch (SecurityException var8) {
                throw new IllegalArgumentException("The security manager does not permit access to the methods of this target.");
            }

            if (this.method == null) {
                StringBuffer error = new StringBuffer();
                error.append("Unable to locate method: public void ");
                error.append(methodName);
                error.append("(");

                for(int i = 0; i < types.length; ++i) {
                    if (i != 0) {
                        error.append(", ");
                    }

                    error.append(simpleClassName(types[i]));
                }

                error.append(")");
                throw new IllegalArgumentException(error.toString());
            }
        }
    }

    private static boolean matches(Method method, String methodName, Class[] types) {
        if (!method.getName().equals(methodName)) {
            return false;
        } else {
            Class[] params = method.getParameterTypes();
            if (params.length != types.length) {
                return false;
            } else {
                for(int i = 0; i < params.length; ++i) {
                    if (!typeMatch(types[i], params[i])) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private static boolean typeMatch(Class valueClass, Class paramClass) {
        if (valueClass == null) {
            return !paramClass.isPrimitive();
        } else if (paramClass.isPrimitive()) {
            if (paramClass == Boolean.TYPE) {
                return valueClass == Boolean.class;
            } else if (paramClass == Character.TYPE) {
                return valueClass == Character.class;
            } else {
                return Number.class.isAssignableFrom(valueClass);
            }
        } else {
            return paramClass.isAssignableFrom(valueClass);
        }
    }

    private static void checkTypes(Object[] values, Method method) {
        Class[] params = method.getParameterTypes();

        for(int i = 0; i < params.length; ++i) {
            if (params[i].isPrimitive() && params[i] != Boolean.TYPE && params[i] != Character.TYPE) {
                Number n = (Number)values[i];
                if (params[i] == Byte.TYPE) {
                    if (!(values[i] instanceof Byte)) {
                        values[i] = new Byte(n.byteValue());
                    }
                } else if (params[i] == Short.TYPE) {
                    if (!(values[i] instanceof Short)) {
                        values[i] = new Short(n.shortValue());
                    }
                } else if (params[i] == Integer.TYPE) {
                    if (!(values[i] instanceof Integer)) {
                        values[i] = new Integer(n.intValue());
                    }
                } else if (params[i] == Long.TYPE) {
                    if (!(values[i] instanceof Long)) {
                        values[i] = new Long(n.longValue());
                    }
                } else if (params[i] == Float.TYPE) {
                    if (!(values[i] instanceof Float)) {
                        values[i] = new Float(n.floatValue());
                    }
                } else if (params[i] == Double.TYPE && !(values[i] instanceof Double)) {
                    values[i] = new Double(n.doubleValue());
                }
            }
        }

    }

    private static String simpleClassName(Class c) {
        String s = c.getName();
        return s.substring(s.lastIndexOf(".") + 1);
    }

    public EventTimer(Object target, String methodName) {
        class NamelessClass_1 implements ActionListener {
            NamelessClass_1() {
            }

            public void actionPerformed(ActionEvent e) {
                EventTimer.this.fire();
            }
        }

        this.listener = new NamelessClass_1();
        this.setTarget(target, methodName, new Object[0]);
    }

    public void setTarget(Object target, String methodName) {
        this.setTarget(target, methodName, new Object[0]);
    }

    public EventTimer(Object target, String methodName, Object arg1) {
        class NamelessClass_1 implements ActionListener {
            NamelessClass_1() {
            }

            public void actionPerformed(ActionEvent e) {
                EventTimer.this.fire();
            }
        }

        this.listener = new NamelessClass_1();
        this.setTarget(target, methodName, new Object[]{arg1});
    }

    public void setTarget(Object target, String methodName, Object arg1) {
        this.setTarget(target, methodName, new Object[]{arg1});
    }

    public EventTimer(Object target, String methodName, Object arg1, Object arg2) {
        class NamelessClass_1 implements ActionListener {
            NamelessClass_1() {
            }

            public void actionPerformed(ActionEvent e) {
                EventTimer.this.fire();
            }
        }

        this.listener = new NamelessClass_1();
        this.setTarget(target, methodName, new Object[]{arg1, arg2});
    }

    public void setTarget(Object target, String methodName, Object arg1, Object arg2) {
        this.setTarget(target, methodName, new Object[]{arg1, arg2});
    }

    public EventTimer(Object target, String methodName, Object arg1, Object arg2, Object arg3) {
        class NamelessClass_1 implements ActionListener {
            NamelessClass_1() {
            }

            public void actionPerformed(ActionEvent e) {
                EventTimer.this.fire();
            }
        }

        this.listener = new NamelessClass_1();
        this.setTarget(target, methodName, new Object[]{arg1, arg2, arg3});
    }

    public void setTarget(Object target, String methodName, Object arg1, Object arg2, Object arg3) {
        this.setTarget(target, methodName, new Object[]{arg1, arg2, arg3});
    }

    public void setDelay(double startDelay, double delay) {
        if (this.timer.isRunning()) {
            this.timer.stop();
            this.internalSetDelay(this.lastFireTime < 0L ? delay : delay - 0.001 * (double)(System.currentTimeMillis() - this.lastFireTime), delay);
            this.timer.start();
            this.timer.setInitialDelay((int)(startDelay * (double)1000.0F + (double)0.5F));
        } else {
            this.internalSetDelay(startDelay, delay);
        }

    }

    private void internalSetDelay(double startDelay, double delay) {
        if (delay > (double)0.0F) {
            this.timer.setRepeats(true);
            this.timer.setDelay((int)(delay * (double)1000.0F + (double)0.5F));
        } else {
            this.timer.setRepeats(false);
        }

        if (startDelay < (double)0.0F) {
            startDelay = (double)0.0F;
        }

        this.timer.setInitialDelay((int)(startDelay * (double)1000.0F + (double)0.5F));
    }

    public void start() {
        this.timer.stop();
        this.internalStart();
    }

    private void internalStart() {
        this.timer.start();
        activeTimers.add(this);
        this.startTime = System.currentTimeMillis();
        this.lastFireTime = this.initialFireTime = -1L;
    }

    public void start(double startDelay, double delay) {
        if (this.timer.isRunning()) {
            this.timer.stop();
        }

        this.internalSetDelay(startDelay, delay);
        this.internalStart();
    }

    public boolean isRunning() {
        return this.timer.isRunning();
    }

    public void stop() {
        activeTimers.remove(this);
        this.timer.stop();
    }

    public double getStartDelay() {
        return 0.001 * (double)this.timer.getInitialDelay();
    }

    public double getDelay() {
        return this.timer.isRepeats() ? 0.001 * (double)this.timer.getDelay() : (double)0.0F;
    }

    public double getTimeSinceStart() {
        return this.startTime < 0L ? (double)0.0F : 0.001 * (double)(System.currentTimeMillis() - this.startTime);
    }

    public double getTimeSinceInitialFiring() {
        return this.initialFireTime < 0L ? (double)0.0F : 0.001 * (double)(System.currentTimeMillis() - this.initialFireTime);
    }

    public double getTimeSincePreviousFiring() {
        return this.lastFireTime < 0L ? (double)0.0F : 0.001 * (double)(System.currentTimeMillis() - this.lastFireTime);
    }

    public boolean isInitialFiring() {
        return this.initialFireTime < 0L;
    }

    public void fire() {
        try {
            if (this.method != null) {
                this.method.invoke(this.target, this.args);
            }
        } catch (IllegalArgumentException var7) {
        } catch (IllegalAccessException var8) {
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
        } finally {
            this.lastFireTime = System.currentTimeMillis();
            if (this.initialFireTime < 0L) {
                this.initialFireTime = this.lastFireTime;
            }

            if (!this.timer.isRepeats()) {
                activeTimers.remove(this);
            }

        }

    }
}
