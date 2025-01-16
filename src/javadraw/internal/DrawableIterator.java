//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.util.ArrayList;

public class DrawableIterator {
    private ArrayList<DrawableInterface> list;
    private int index;

    protected DrawableIterator(ArrayList<DrawableInterface> drawables) {
        if (drawables != null) {
            this.list = new ArrayList<>(drawables);
        }

    }

    public boolean hasNext() {
        return this.list != null && this.index < this.list.size();
    }

    public DrawableInterface next() {
        if (!this.hasNext()) {
            throw new IndexOutOfBoundsException("No more drawables in this iterator!");
        } else {
            return (DrawableInterface)this.list.get(this.index++);
        }
    }
}
