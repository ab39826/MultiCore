package q3;

// TODO
// Use MyLock to protect the count

public class LockCounter extends Counter {
	
	MyLock lock;
	
    public LockCounter(MyLock lock) {
    	this.lock = lock;
    }

    @Override
    public void increment() {
    	lock.lock((int)Thread.currentThread().getId());
    	try{
    		count++;
//    		System.out.println("Count: " + count + " thread: " + (int)Thread.currentThread().getId());
    	}
    	finally{
    		lock.unlock((int)Thread.currentThread().getId());
    	}
    }
    
    @Override
    public int getCount() {
        return count;
    }
}
