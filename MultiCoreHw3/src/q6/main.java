package q6;

import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;


public class main {
    public static void main (String[] args) throws InterruptedException {
        Counter counter = null;
        MyLock lock;
        long executeTimeMS = 0;
        int numThread = 4;
        int numTotalInc = 1200000;
        int increments;
        
        if (args.length < 1) {
            System.err.println("Provide 3 arguments");
            System.err.println("\t(1) <algorithm>: fast/bakery/synchronized/"
                    + "reentrant");
            System.err.println("\t(2) <numThread>: the number of test thread");
            System.err.println("\t(3) <numTotalInc>: the total number of "
                    + "increment operations performed");
            System.exit(-1);
        }
        
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

        //numThread = Integer.parseInt(args[1]);
        //numTotalInc = Integer.parseInt(args[2]);
        increments = numTotalInc / numThread;
        
        // TODO
        // Please create numThread threads to increment the counter
        // Each thread executes numTotalInc/numThread increments
        // Please calculate the total execute time in millisecond and store the
        // result in executeTimeMS
        
        long startTime = System.currentTimeMillis();        
        
		ArrayList<Thread> myThreads = new ArrayList<Thread>();
		
		int incSoFar = 0;
		for (int i = 0; i < numThread; i++) {
			if(i == numThread-1){
				myThreads.add(new Thread(new Worker((numTotalInc - incSoFar), counter)));
				myThreads.get(i).start();
			}
			else{
			myThreads.add(new Thread(new Worker(increments, counter)));
			myThreads.get(i).start();
			incSoFar += increments;
			}
		}

		for (int i = 0; i < numThread; i++) {
			try {
				myThreads.get(i).join();
//				System.out.println("Joined thread " + i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
                
        long stopTime = System.currentTimeMillis();
        executeTimeMS = stopTime - startTime;
        System.out.println("Count: " + counter.count);
        System.out.println("Time:" + executeTimeMS);
    }
}
