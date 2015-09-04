package q3goober;

import java.util.concurrent.locks.ReentrantLock;

// TODO
// Use ReentrantLock to protect the count
public class ReentrantCounter extends Counter {
	private final ReentrantLock lock = new ReentrantLock();
	int counter;
	int myId;
	
	public void addThread(long ID, int myID){
		
	}
	
	@Override
    public void increment() {
		lock.lock();
		try{
			counter++;
		}finally {
			lock.unlock();
		}
    }

    @Override 
    public int getCount() {
        return counter;
    }
}
