//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package javadraw.internal;

public class WindowThread extends Thread {
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

    public void run() {
        super.run();
    }

    public void start() {
        super.start();
    }

    public void kill() {
        this.killed = true;
        this.interrupt();
    }

    private static void checkKilled() {
        Thread active = Thread.currentThread();
        if (active instanceof ActiveObject && ((WindowThread)active).killed) {
            throw new ThreadDeath();
        }
    }

    public static void yield() {
        checkKilled();
        Thread.yield();
        checkKilled();
    }

    public void pause(long millis, int nanos) {
        checkKilled();

        try {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException ignored) {
        }

        checkKilled();
    }

    public void pause(double millis) {
        this.pause((long)millis, 0);
    }

    public void pause(long millis) {
        this.pause(millis, 0);
    }

    public WindowThread() {
        this("WindowThread" + id++);
    }

    public WindowThread(String name) {
        super(GROUP, name);
        this.killed = false;
    }

    public WindowThread(Runnable target) {
        this(target, "WindowThread" + id++);
    }

    public WindowThread(Runnable target, String name) {
        super(GROUP, target, name);
        this.killed = false;
    }
}
