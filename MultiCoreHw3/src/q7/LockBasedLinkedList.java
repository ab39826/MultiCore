package q7;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockBasedLinkedList implements iLinkedList {
	
	private Node head;
	AtomicLong addTime = new AtomicLong(0);
	AtomicInteger adds = new AtomicInteger(0);
	AtomicLong removeTime = new AtomicLong(0);
	AtomicInteger removes = new AtomicInteger(0);
	AtomicLong containsTime = new AtomicLong(0);
	AtomicInteger contains = new AtomicInteger(0);
	
	
	public LockBasedLinkedList(){
		head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
		System.out.println("Butt");
	}

	@Override
	public boolean add(Integer x) {
		long StartTime = System.nanoTime();
		int key = x.hashCode();
		head.lock.lock();
		Node pred = head;
		try {
			Node curr = pred.next;
			curr.lock.lock();
			try {
			while(curr.key < key){
				pred.lock.unlock();
				pred = curr;
				curr = curr.next;
				curr.lock.lock();
			}
			
			if (curr.key == key) {
				return false;
			} 
				Node newNode = new Node(x);
				newNode.next = curr;
				pred.next = newNode;
				return true;
			}
		 finally {
			curr.lock.unlock();
		}
			} finally {
				adds.getAndIncrement();
				addTime.getAndAdd(System.nanoTime()-StartTime);
				pred.lock.unlock();
			}
		
	}

	@Override
	public boolean remove(Integer x) {
		long StartTime = System.nanoTime();
		Node pred = null, curr = null;
		int key = x.hashCode();
		
		head.lock.lock();
		try {
			pred = head;
			curr = pred.next;
			curr.lock.lock();
			try {
				while (curr.key < key) {
					pred.lock.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock.lock();
				}
				if (curr.key == key){
					pred.next = curr.next;
					return true;
				}
				return false;
			} finally {
				curr.lock.unlock();
			}
		} finally {
			removes.getAndIncrement();
			removeTime.getAndAdd(System.nanoTime()-StartTime);
			pred.lock.unlock();
		}
	}

	@Override
	public boolean contains(Integer x) {
		long StartTime = System.nanoTime();
		Node pred = null, curr = null;
		int key = x.hashCode();
		
		head.lock.lock();
		try {
			pred = head;
			curr = pred.next;
			curr.lock.lock();
			try {
				while (curr.key < key) {
					pred.lock.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock.lock();
				}
				if (curr.key == key){
					return true;
				}
				return false;
			} finally {
				curr.lock.unlock();
			}
		} finally {
			contains.getAndIncrement();
			containsTime.getAndAdd(System.nanoTime()-StartTime);
			pred.lock.unlock();
		}
	}
	
	public void printContents(){
		Node curr = head;
		int i = 0;
		
		while(curr.next != null){
			System.out.println("Node " + i + " equals " + curr.item);
			i++;
			curr = curr.next;
		}
	}
	
	public long getAddTime(){
		return addTime.get();
	}
	public long getRemoveTime(){
		return removeTime.get();
	}
	public long getContainsTime(){
		return containsTime.get();
	}
	public int getAdds(){
		return adds.get();
	}
	public int getRemoves(){
		return removes.get();
	}
	public int getContains(){
		return contains.get();
	}
	
	
	
	private static class Node {
		Integer item;
		int key;
		Node next;
		Lock lock;
		
		Node(Integer x){
			item = x;
			key = x.hashCode();
			lock = new ReentrantLock();
		}
	}
	
}