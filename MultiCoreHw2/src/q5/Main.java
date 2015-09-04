package q5;

public class Main {
	public static void main(String[] args) {
		BathroomLockProtocol bathroom = new BathroomLockProtocol();
		
		Thread myThread = new Thread(new Worker(true, bathroom));
		myThread.start();
		Thread myThread2 = new Thread(new Worker(false, bathroom));
		myThread2.start();
		
		Thread myThread3 = new Thread(new Worker(false, bathroom));
		myThread3.start();
		
		Thread myThread4 = new Thread(new Worker(true, bathroom));
		myThread4.start();
		Thread myThread5 = new Thread(new Worker(true, bathroom));
		myThread5.start();
		Thread myThread6 = new Thread(new Worker(true, bathroom));
		myThread6.start();
	
	}
	
}
