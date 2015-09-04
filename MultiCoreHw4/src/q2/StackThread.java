package q2;

import java.util.Random;

public class StackThread extends Thread{
	iStack stack;
	volatile boolean running=true;
	volatile boolean running2 = true;
	volatile int iterations = 0;
	Random rand = new Random();
	volatile boolean starting = false;
	int op = 0;
	int pushs = 0;
	int pops = 0;
	
	
	public void run(){
		for(int i = 0;i<iterations; i++){
			stack.push(rand.nextInt(100000));
			pushs++;
		}
		running = false; 

		while (!starting){;}
		
		for(int i = 0;i<iterations; i++){
			op = rand.nextInt(10);
			if(op < 6){
				stack.push(rand.nextInt(100000));
				pushs++;
			}
			else{
				stack.pop();
				pops++;
			} 
		}
		
		
		
		running2 = false;
		
		while(!running2){}
		
		System.out.println("This thread had "+pushs+" pushes and "+pops+" pops, for a total of "+(pushs + pops)+" ops.");
	}
	
	
	public boolean isRunning(){
		return running;
	}
	
	public boolean isRunning2(){
		return running2;
	}
}