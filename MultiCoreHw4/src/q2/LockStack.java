package q2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;





public class LockStack implements iStack {
	
	
	ReentrantLock opLock;
	
	volatile private Node base;
	volatile private Node top;
	
	AtomicInteger size;
	
	//used for profiling throughput of the application
		AtomicLong pushTime = new AtomicLong(0);
		AtomicInteger pushs = new AtomicInteger(0);
		AtomicLong popTime = new AtomicLong(0);
		AtomicInteger pops = new AtomicInteger(0);
	
	public LockStack(){
		
		base = new Node(null);
		top = base;
		
		opLock = new ReentrantLock();
		
		size = new AtomicInteger(0);
	}
	
	
	public void push(Integer x){
		long StartTime = System.nanoTime();
		
		opLock.lock();
		
		try {
			Node e = new Node(x);
			e.prev = top;
			top.next = e;
			top = e;
		} finally {
			pushTime.getAndAdd(System.nanoTime()-StartTime);
			pushs.getAndIncrement();
			size.getAndIncrement();
			opLock.unlock();
		}
		
		
	}
	
	public Integer pop(){

		long StartTime = System.nanoTime();
		opLock.lock();
		Integer returnVal;
		try{
			if(top == base){
				
				return null;
			}
			
			else{
				
				returnVal = top.value;
				top = top.prev;
				top.next = null;
			}
		}
		finally{
			popTime.getAndAdd(System.nanoTime()-StartTime);
			pops.getAndIncrement();
			size.getAndDecrement();
			opLock.unlock();
			
			
		}
		
		return returnVal;
	}
	
	public long getPushTime(){
		return pushTime.get();
	}
	public long getPopTime(){
		return popTime.get();
	}

	public int getPushs(){
		return pushs.get();
	}
	public int getPops(){
		return pops.get();
	}
	
	private static class Node {
		Integer value;
		Node next;
		Node prev;
		
		public Node(Integer x){
			value = x;
			next = null;
			prev = null;
		}
		
	}
	
}