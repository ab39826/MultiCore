package q2;

import java.util.Random;

public class QueueThread extends Thread{
	iQueue queue;
	volatile boolean running=true;
	volatile boolean running2 = true;
	volatile int iterations = 0;
	Random rand = new Random();
	volatile boolean starting = false;
	int op = 0;
	int enqs = 0;
	int deqs = 0;
	
	
	public void run(){
		for(int i = 0;i<iterations; i++){
			queue.enqueue(rand.nextInt(100000));
			enqs++;
		}
		running = false; 

		while (!starting){;}
		
		for(int i = 0;i<iterations; i++){
			op = rand.nextInt(10);
			if(op < 6){
				queue.enqueue(rand.nextInt(100000));
				enqs++;
			}
			else{
				queue.dequeue();
				deqs++;
			} 
		}
		
		
		
		running2 = false;
		
		while(!running2){}
		
		System.out.println("This thread had "+enqs+" pushes and "+deqs+" pops, for a total of "+(enqs + deqs)+" ops.");
	}
	
	
	public boolean isRunning(){
		return running;
	}
	
	public boolean isRunning2(){
		return running2;
	}
}