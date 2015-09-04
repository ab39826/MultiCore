package q3goober;

public class Main {
    public static void main (String[] args) {
        Counter counter;
        MyLock lock;
        long executeTimeMS = 0;
        int numThread = 6;
        int numTotalInc = 1200000;
        MyThread[] Threads;

        if (args.length < 3) {
            System.err.println("Provide 3 arguments");
            System.err.println("\t(1) <algorithm>: fast/bakery/synchronized/"
                    + "reentrant");
            System.err.println("\t(2) <numThread>: the number of test thread");
            System.err.println("\t(3) <numTotalInc>: the total number of "
                    + "increment operations performed");
            System.exit(-1);
        }
        
        numThread = Integer.parseInt(args[1]);
        numTotalInc = Integer.parseInt(args[2]);
        
        counter = new ReentrantCounter();
        if (args[0].equals("fast")) {
            lock = new FastMutexLock(numThread);
            counter = new LockCounter(lock);
        } else if (args[0].equals("bakery")) {
            lock = new BakeryLock(numThread);
            counter = new LockCounter(lock);
        } else if (args[0].equals("synchronized")) {
            counter = new SynchronizedCounter();
        } else if (args[0].equals("reentrant")) {
            counter = new ReentrantCounter();
        } else {
            System.err.println("ERROR: no such algorithm implemented");
            System.exit(-1);
        }

        // System.out.println("Counting with threads: " + numThread);
        // System.out.println("Counting until: " + numTotalInc);
        
        Threads = new MyThread[numThread];
        
        long StartTime = System.nanoTime();
        
        for(int i=0;i<numThread;i++){
        	Threads[i] = new MyThread();
        	Threads[i].iterations = numTotalInc/numThread;
        	Threads[i].counter = counter;
        	Threads[i].myId = i;
        	
        	if(i == numThread - 1)
        		Threads[i].iterations += numTotalInc % numThread;
        	
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
        long EndTime = System.nanoTime();
        executeTimeMS = (EndTime - StartTime)/1000000;

        System.out.println(executeTimeMS);
    }
}