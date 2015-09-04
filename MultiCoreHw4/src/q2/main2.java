package q2;



public class main2{
	public static void main(String[] args){
		int numThreads = 6;
		iQueue queue;
		QueueThread[] Threads;
		int numEnqs = 1200;
		int numOps = 1200000;
		
		

		
		//numThreads = Integer.parseInt(args[1]);
		
		
		queue = new UnboundedLockBasedQueue();
		if(args[0].equals("lock")){
			queue = new UnboundedLockBasedQueue();
		}else if(args[0].equals("free")){
			queue = new UnboundedLockFreeQueue();
		}else {
			System.out.println("Specify queue!\n");
			System.exit(-1);
		}
		
		
		
		Threads = new QueueThread[numThreads];
		
		for(int i=0;i<numThreads;i++){
			Threads[i] = new QueueThread();
			Threads[i].iterations = numEnqs / numThreads;
			Threads[i].queue = queue;
			if(i == numThreads - 1){
        		Threads[i].iterations += numEnqs % numThreads;
			}
			Threads[i].start();
		}
		
		// First do 5000 adds
		boolean going = true;
		while (going){
			going = false;
			for(int i=0;i<numThreads;i++){
				if(Threads[i].isRunning()){
					going = true;
				}
			}
		}
		
		// Then, do 25000 random ops
		for(int i=0;i<numThreads;i++){
			Threads[i].iterations = numOps / numThreads;
			if(i == numThreads - 1)
        		Threads[i].iterations += numOps % numThreads;
			Threads[i].starting = true;
		}
		
		going = true;
		while (going){
			going = false;
			for(int i=0;i<numThreads;i++){
				if(Threads[i].isRunning2()){
					going = true;
				}
			}
		}
		
		//((LockBasedLinkedList) list).printContents();
		
		for(int i=0;i<numThreads;i++){
			Threads[i].running2 = true;
		}
		
		
		System.out.println("Enqueues: "+ queue.getEnqs()+", Dequeues: "+queue.getDeqs());
		System.out.println("Total ops: "+(queue.getEnqs()+queue.getDeqs()));
		System.out.println("Total enq time was "+queue.getEnqTime()+"ns, average enq time was "+queue.getEnqTime()/(queue.getEnqs())+"ns");
		System.out.println("Total deq time was "+queue.getDeqTime()+"ns, average deq time was "+queue.getDeqTime()/(queue.getDeqs())+"ns");
		
		System.out.println("Latency "+ (1.0/1000000)*(queue.getEnqTime() + queue.getDeqTime())
				/((queue.getEnqs()+queue.getDeqs())));
		
		
		//System.out.println("Thoroughput " + (1.0)*((list.getAdds()+list.getRemoves()+list.getContains())/((list.getAddTime() + list.getRemoveTime() + list.getContainsTime()))));
	}
}