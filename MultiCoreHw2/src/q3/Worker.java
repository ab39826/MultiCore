package q3;

public class Worker implements Runnable {

	private int inc;
	private Counter counter;

	public Worker(int increments, Counter counter) {
		this.inc = increments;
		this.counter = counter;
	}

	@Override
	public void run() {
		for (int i = 0; i < inc; i++) {
			counter.increment();
		}
		return;
	}
}