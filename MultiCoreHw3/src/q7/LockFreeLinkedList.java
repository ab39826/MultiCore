package q7;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeLinkedList implements iLinkedList {

	private Node head;
	AtomicLong addTime = new AtomicLong(0);
	AtomicInteger adds = new AtomicInteger(0);
	AtomicLong removeTime = new AtomicLong(0);
	AtomicInteger removes = new AtomicInteger(0);
	AtomicLong containsTime = new AtomicLong(0);
	AtomicInteger contains = new AtomicInteger(0);
	
	public LockFreeLinkedList(){
		head = new Node(0);
		head.next = new AtomicMarkableReference<Node>(new Node(100000),false);
	}
	
	@Override
	public boolean add(Integer x) {
		boolean result = false;
		long StartTime = System.nanoTime();
		
		result = head.add(x);
		
		adds.getAndIncrement();
		addTime.getAndAdd(System.nanoTime()-StartTime);
		return result;
	}

	@Override
	public boolean remove(Integer x) {
		boolean result = false;
		long StartTime = System.nanoTime();
		
		result = head.remove(x);
		
		removes.getAndIncrement();
		removeTime.getAndAdd(System.nanoTime()-StartTime);
		return result;
	}

	@Override
	public boolean contains(Integer x) {
		boolean result = false;
		long StartTime = System.nanoTime();
		
		result = head.contains(x);
		
		contains.getAndIncrement();
		containsTime.getAndAdd(System.nanoTime()-StartTime);
		return result;
	}
	
	public void printContents(){
		Node curr = head;
		int i = 0;
		
		while(curr.next != null){
			System.out.println("Node " + i + " equals " + curr.item);
			i++;
			curr = curr.next.getReference();
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

}