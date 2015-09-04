package q3goober;

// TODO 
// Use synchronized to protect count
public class SynchronizedCounter extends Counter {
	int counter;
	int myId;
	
	public void addThread(long ID, int myID){
		
	}
	
    @Override
    public synchronized void increment() {
		counter++;
	}

    @Override 
    public int getCount() {
        return count;
    }
}
