//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public class RandomDoubleGenerator {
    private double min;
    private double max;

    public RandomDoubleGenerator(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double nextValue() {
        return Math.random() * (this.max - this.min) + this.min;
    }
}
