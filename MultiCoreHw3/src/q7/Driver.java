package q7;

public class Driver{
	public static void main(String[] args){
		int numThreads = 5;
		iLinkedList list;
		ListThread[] Threads;
		int numAdds = 5000;
		int numOps = 25000;
		
		if(args.length != 2){
			System.out.println("First, specify 'lock' or 'nonlock', then specify number of threads to execute!\n");
			System.exit(-1);
		}
		
		//numThreads = Integer.parseInt(args[1]);
		
		list = new LockBasedLinkedList();
		if(args[0].equals("lock")){
			list = new LockBasedLinkedList();
		}else if(args[0].equals("nonlock")){
			list = new LockFreeLinkedList();
		}else {
			System.out.println("Specify lock or nonlock!\n");
			System.exit(-1);
		}
		
		Threads = new ListThread[numThreads];
		
		for(int i=0;i<numThreads;i++){
			Threads[i] = new ListThread();
			Threads[i].iterations = numAdds / numThreads;
			Threads[i].list = list;
			if(i == numThreads - 1){
        		Threads[i].iterations += numAdds % numThreads;
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
		
		
		System.out.println("Adds: "+list.getAdds()+", Removes: "+list.getRemoves()+", Contains: "+list.getContains());
		System.out.println("Total ops: "+(list.getAdds()+list.getRemoves()+list.getContains()));
		System.out.println("Total add time was "+list.getAddTime()+"ns, average add time was "+list.getAddTime()/(list.getAdds())+"ns");
		System.out.println("Total remove time was "+list.getRemoveTime()+"ns, average remove time was "+list.getRemoveTime()/(list.getRemoves())+"ns");
		System.out.println("Total contains time was "+list.getContainsTime()+"ns, average contains time was "+list.getContainsTime()/(list.getContains())+"ns");
		
		
		System.out.println("Latency "+ (1.0/1000000)*(list.getAddTime() + list.getRemoveTime() + list.getContainsTime())
				/((list.getAdds()+list.getRemoves()+list.getContains())));
		
		//System.out.println("Thoroughput " + (1.0)*((list.getAdds()+list.getRemoves()+list.getContains())/((list.getAddTime() + list.getRemoveTime() + list.getContainsTime()))));
	}
}