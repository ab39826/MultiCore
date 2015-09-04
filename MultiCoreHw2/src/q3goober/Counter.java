package q3goober;

public abstract class Counter {
    public Counter() {
        count = 0;
    }
    protected volatile int count;
    public abstract void addThread(long ID, int myID);
    public abstract void increment();
    public abstract int getCount();
}
