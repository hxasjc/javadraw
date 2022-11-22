//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/** @deprecated */
@Deprecated
public class ObjectDrawObject implements Dependent, Serializable {
    private static LinkedList notify = new LinkedList();
    private static boolean defer = false;
    HashSet dependents = new HashSet();

    public ObjectDrawObject() {
    }

    /** @deprecated */
    @Deprecated
    public void depend(Dependent dependent) {
        if (dependent != null) {
            this.dependents.add(dependent);
        }

    }

    /** @deprecated */
    @Deprecated
    public void undepend(Dependent dependent) {
        if (dependent != null) {
            this.dependents.remove(dependent);
        }

    }

    /** @deprecated */
    @Deprecated
    public static void deferUpdates() {
        defer = true;
    }

    /** @deprecated */
    @Deprecated
    public static synchronized void runUpdates() {
        defer = false;

        for(int i = 0; i < notify.size(); ++i) {
            ((Dependent)notify.get(i)).update();
        }

        notify.clear();
    }

    /** @deprecated */
    @Deprecated
    private static synchronized void triggerUpdate(ObjectDrawObject object) {
        if (!defer && notify.size() <= 0) {
            notify.addAll(object.dependents);
            runUpdates();
        } else {
            Iterator iter = object.dependents.iterator();

            while(iter.hasNext()) {
                Dependent dependent = (Dependent)iter.next();
                if (!notify.contains(dependent)) {
                    notify.addLast(dependent);
                }
            }
        }

    }

    /** @deprecated */
    @Deprecated
    public void update() {
        triggerUpdate(this);
    }

    /** @deprecated */
    @Deprecated
    protected String toString(String middle) {
        String name = this.getClass().getName();
        return "new " + name.substring(name.indexOf(".") + 1) + "(" + middle + ")";
    }

    public String toString() {
        return this.toString("");
    }
}
