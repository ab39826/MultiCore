package q3goober;

// TODO
// Use MyLock to protect the count

public class LockCounter extends Counter {
	protected volatile int count = 0;
	volatile MyLock lock;
	volatile long[] IDs;
	int numThreads = 0;
	
	public synchronized void addThread(long ID, int myID){
		int i;
		if(numThreads == 0){
			IDs = new long[numThreads+1];
			IDs[0] = ID;
			//System.out.println("New Thread : "+ID+"\n");
		}else {
			long[] threads=new long[numThreads];
			for(int j=0;j<numThreads;j++){
				threads[j] = IDs[j];
			}
			IDs = new long[numThreads+1];
			//System.out.println("New Thread : "+ID+"\n");
			for(i=0;i<numThreads;i++){
				//System.out.println("Thread "+threads[i]+"\n");
				IDs[i] = threads[i];
			}
			IDs[i] = ID;
		}
		numThreads++;
	}
	
    public LockCounter(MyLock l) {
    	lock = l;
    	count = 0;
    }
    
    @Override
    public void increment() {
    	int thread=0;
    	for(int i=0;i<numThreads;i++){
    		if(IDs[i] == MyThread.currentThread().getId()){
    			thread = i;
    		}
    	}
    	lock.lock(thread);
    	count++;
    	//System.out.println("Counter is " + count + " by thread: " + thread);
    	lock.unlock(thread);
    }

    @Override
    public int getCount() {
        return count;
    }
}
