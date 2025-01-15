//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public class RandomIntGenerator {
    private int min;
    private int max;

    public RandomIntGenerator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int nextValue() {
        return (int)(Math.random() * (double)(this.max - this.min + 1)) + this.min;
    }
}
