package q3goober;

// TODO
// Implement the bakery algorithm

public class BakeryLock implements MyLock {
	volatile boolean[] flag;
	volatile int[] label;
	volatile int Threads;
    public BakeryLock(int numThread) {
    	flag = new boolean[numThread];
    	label = new int[numThread];
    	for(int i=0;i<numThread;i++){
    		flag[i] = false;
    		label[i] = 0;
    	}
    	Threads = numThread;
    }

    @Override
    public void lock(int myId) {
    	flag[myId]=true;
    	int max = 0;
    	boolean waiting = true;
    	for(int i=0;i<Threads;i++){
    		if(label[i]>max){
    			max = label[i];	
    		}	
    	}
    	label[myId] = max+1;
    	while(waiting){
    		waiting = false;
    		for(int i=0;i<Threads;i++){
    			if(flag[i]==true && (label[i]<label[myId] || (label[i] == label[myId] && i<myId))){
    				waiting = true;
    			}
    		}
    	}
    }

    @Override
    public void unlock(int myId) {
    	flag[myId] = false;
    }
}
