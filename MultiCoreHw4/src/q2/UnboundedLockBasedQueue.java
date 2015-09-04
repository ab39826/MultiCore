package q2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class UnboundedLockBasedQueue implements iQueue {
	ReentrantLock enqLock;
	ReentrantLock deqLock;
	volatile private Node head;
	volatile private Node tail;
	
	//used for profiling throughput of the application
	AtomicLong enqTime = new AtomicLong(0);
	AtomicInteger enqs = new AtomicInteger(0);
	AtomicLong deqTime = new AtomicLong(0);
	AtomicInteger deqs = new AtomicInteger(0);
	
	public UnboundedLockBasedQueue(){
		head = new Node(null);
		tail = head;
				
		enqLock = new ReentrantLock();
		deqLock = new ReentrantLock();
		
	
		
	}
	
	public void enqueue(Integer x) {
		long StartTime = System.nanoTime();
		
		enqLock.lock();
		
		try {
			Node e = new Node(x);
			tail.next = e;
			tail = e;
		} finally {
			enqTime.getAndAdd(System.nanoTime()-StartTime);
			enqs.getAndIncrement();
			enqLock.unlock();
		}
		
		
	}
	
	
	public Integer dequeue() {
		long StartTime = System.nanoTime();
		Integer result;
		
		deqLock.lock();
		
		try {
			if (head.next == null) {
				return Integer.MIN_VALUE;
			}
		
			result = head.next.value;
			head = head.next;
		} finally {
			deqTime.getAndAdd(System.nanoTime()-StartTime);
			deqs.getAndIncrement();
			deqLock.unlock();
		}
		
		return result;
	}
	
	
	public long getEnqTime(){
		return enqTime.get();
	}
	public long getDeqTime(){
		return deqTime.get();
	}

	public int getEnqs(){
		return enqs.get();
	}
	public int getDeqs(){
		return deqs.get();
	}
	
	
	private static class Node {
		public Integer value;
		public volatile Node next;
		
		public Node(Integer x){
			value = x;
			next = null;
		}
	}
	
}