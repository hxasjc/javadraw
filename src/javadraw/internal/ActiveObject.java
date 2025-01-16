//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

@SuppressWarnings("unused")
public class ActiveObject extends Thread {
    private static int id = 1;
    private boolean killed;
    private static final ThreadGroup GROUP = new ThreadGroup("javaDraw");

    public static void killAll() {
        Thread[] threads = new Thread[GROUP.activeCount() + 20];
        int length = GROUP.enumerate(threads);

        for(int i = 0; i < length; ++i) {
            ((ActiveObject)threads[i]).kill();
        }

    }

    public void kill() {
        this.killed = true;
        this.interrupt();
    }

    private static void checkKilled() {
        Thread active = Thread.currentThread();
        if (active instanceof ActiveObject && ((ActiveObject)active).killed) {
            throw new ThreadDeath();
        }
    }

    public static void yield() {
        checkKilled();
        Thread.yield();
        checkKilled();
    }

    public static void pause(long millis, int nanos) {
        checkKilled();

        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException ignored) {}

        checkKilled();
    }

    public static void pause(double millis) {
        pause((long)millis, 0);
    }

    public static void pause(long millis) {
        pause(millis, 0);
    }

    public ActiveObject() {
        this("ActiveObject" + id++);
    }

    public ActiveObject(String name) {
        super(GROUP, name);
        this.killed = false;
    }

    public ActiveObject(Runnable target) {
        this(target, "ActiveObject" + id++);
    }

    public ActiveObject(Runnable target, String name) {
        super(GROUP, target, name);
        this.killed = false;
    }
}
