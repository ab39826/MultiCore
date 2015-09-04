package q3goober;

public class MyThread extends Thread{
	Counter counter;
	int iterations;
	int myId = 0;
	boolean running = true;
	
	public void run(){
		running = true;
		int i;
		counter.addThread(Thread.currentThread().getId(),myId);
		for(i=0; i<iterations;i++){
			counter.increment();
		}
		running = false;
	}

	public int setID(){
		return myId;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public static void main(String args[]){
		
	}
	
}