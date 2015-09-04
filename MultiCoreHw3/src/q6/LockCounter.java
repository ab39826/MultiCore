package q6;

import q6.Counter;
import q6.MyLock;

// TODO
// Use MyLock to protect the count

public class LockCounter extends Counter {
	
	MyLock lock;
	
    public LockCounter(MyLock lock) {
    	this.lock = lock;
    }

    @Override
    public void increment() {
    	lock.lock();
    	try{
    		count++;
    	}
    	finally{
    		lock.unlock();
    	}
    }
    
    @Override
    public int getCount() {
        return count;
    }
}
