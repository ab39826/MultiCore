package q7;

import java.util.Random;

public class ListThread extends Thread{
	iLinkedList list;
	volatile boolean running=true;
	volatile boolean running2 = true;
	volatile int iterations = 0;
	Random rand = new Random();
	volatile boolean starting = false;
	int op = 0;
	int adds = 0;
	int removes = 0;
	int contains = 0;
	
	public void run(){
		for(int i = 0;i<iterations; i++){
			list.add(rand.nextInt(100000));
			adds++;
		}
		running = false; 

		while (!starting){;}
		
		for(int i = 0;i<iterations; i++){
			op = rand.nextInt(10);
			if(op == 0){
				list.remove(rand.nextInt(100000));
				removes++;
			}
			else if (op < 6){
				list.contains(rand.nextInt(100000));
				contains++;
			} else{
				list.add(rand.nextInt(100000));
				adds++;
			}
		}
		
		
		
		running2 = false;
		
		while(!running2){}
		
		System.out.println("This thread had "+adds+" adds, "+removes+" removes, and "+contains+" contains, for a total of "+(adds+removes+contains)+" ops.");
	}
	
	
	public boolean isRunning(){
		return running;
	}
	
	public boolean isRunning2(){
		return running2;
	}
}