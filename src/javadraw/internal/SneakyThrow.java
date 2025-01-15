//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

import java.io.IOException;

public class SneakyThrow {
    public SneakyThrow() {
    }

    public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        //throw e;
        throw new RuntimeException(e);
    }

    private static void throwsSneakyIOException() {
        sneakyThrow(new IOException("sneaky"));
    }
}
