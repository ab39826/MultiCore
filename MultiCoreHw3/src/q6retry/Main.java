package q6retry;

import q6retry.CLHLock;
import q6retry.LockCounter;
import q6retry.MCSLock;
import q6retry.constants;

public class Main {
   
	public static void main (String[] args) {
        Counter counter = null;
        MyLock lock;
        long executeTimeMS = 0;
        int numThread = 4;
        int numTotalInc = 1200000;
        MyThread[] Threads;
        int inc = constants.inc;

        if (args.length < 3) {
            System.err.println("Provide 3 arguments");
            System.err.println("\t(1) <algorithm>: fast/bakery/synchronized/"
                    + "reentrant");
            System.err.println("\t(2) <numThread>: the number of test thread");
            System.err.println("\t(3) <numTotalInc>: the total number of "
                    + "increment operations performed");
            System.exit(-1);
        }
        
        //numThread = Integer.parseInt(args[1]);
        //numTotalInc = Integer.parseInt(args[2]);
        
        /*
        if (args[0].equals("CLH")) {
            lock = new CLHLock();
            counter = new LockCounter(lock);
        } 
        else if (args[0].equals("MCS")){
        	lock = new MCSLock();
        	counter = new LockCounter(lock);
        	System.out.println("Butts");
        }
        else {
            System.err.println("ERROR: no such algorithm implemented");
            System.exit(-1);
        }
		*/
        
        lock = new CLHLock();
        counter = new LockCounter(lock);
        System.out.println("Inc is " + inc);
		
        // System.out.println("Counting with threads: " + numThread);
        // System.out.println("Counting until: " + numTotalInc);
        long startTime = System.currentTimeMillis();  
        Threads = new MyThread[numThread];
        
        
        
        for(int i=0;i<numThread;i++){
        	Threads[i] = new MyThread();
        	Threads[i].iterations = (numTotalInc/inc)/numThread;
        	Threads[i].counter = counter;
        	Threads[i].myId = i;
        	
        	if(i == numThread - 1)
        		Threads[i].iterations += (numTotalInc/inc) % numThread;
        	
        	Threads[i].start();
        }
        
        //Wait for threads to finish
        boolean going=true;
        while(going){
        	going = false;
        	for(int i=0;i<numThread;i++){
        		if(Threads[i].isRunning()){
        			going = true;
        		}
        	}
        }
        
        //Calculate time
        
        long stopTime = System.currentTimeMillis();
        executeTimeMS = stopTime - startTime;
        System.out.println("Count: " + counter.getCount());
        System.out.println("Time:" + executeTimeMS);
    }
}