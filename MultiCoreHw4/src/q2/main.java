package q2;



public class main{
	public static void main(String[] args){
		int numThreads = 6;
		iStack stack;
		StackThread[] Threads;
		int numPushes = 12000;
		int numOps = 1200000;
		
		
		
		
	
		
		//numThreads = Integer.parseInt(args[1]);
		
		
		stack = new LockStack();
		if(args[0].equals("lock")){
			stack = new LockStack();
		}else if(args[0].equals("backoff")){
			stack = new LockFreeStackBackoff();
		}
		else if(args[0].equals("elimination")){
			stack = new LockFreeStackElimination();
		}
		else {
			System.out.println("Specify stack!\n");
			System.exit(-1);
		}
		
		
		
		Threads = new StackThread[numThreads];
		
		for(int i=0;i<numThreads;i++){
			Threads[i] = new StackThread();
			Threads[i].iterations = numPushes / numThreads;
			Threads[i].stack = stack;
			if(i == numThreads - 1){
        		Threads[i].iterations += numPushes % numThreads;
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
		
		System.out.println("Pushs: "+ stack.getPushs()+", Pops: "+stack.getPops());
		System.out.println("Total ops: "+(stack.getPushs()+stack.getPops()));
		System.out.println("Total push time was "+stack.getPushTime()+"ns, average push time was "+stack.getPushTime()/(stack.getPushs())+"ns");
		System.out.println("Total pop time was "+stack.getPopTime()+"ns, average pop time was "+stack.getPopTime()/(stack.getPops())+"ns");
		
		System.out.println("Latency "+ (1.0/1)*(stack.getPushTime() + stack.getPopTime())
				/((stack.getPushs()+stack.getPops())));
		//System.out.println("Thoroughput " + (1.0)*((list.getAdds()+list.getRemoves()+list.getContains())/((list.getAddTime() + list.getRemoveTime() + list.getContainsTime()))));
	}
}