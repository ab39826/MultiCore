package q3;

import java.util.concurrent.locks.ReentrantLock;

// TODO 
// Use synchronized to protect count
public class SynchronizedCounter extends Counter {

	ReentrantLock lock = new ReentrantLock();

	@Override
	public void increment() {
		synchronized (lock) {
				count = getCount();
				count++;
		}
	}
	

	@Override
	public int getCount() {
		return count;
	}
}
