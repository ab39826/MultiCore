package q2;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStackBackoff implements iStack {
    AtomicReference<Node> top = new AtomicReference<Node>(null);
    
    
    static final int MIN_DELAY = 1;
    static final int MAX_DELAY = 100;
    Backoff backoff = new Backoff(MIN_DELAY, MAX_DELAY);
    
  //used for profiling throughput of the application
  		AtomicLong pushTime = new AtomicLong(0);
  		AtomicInteger pushs = new AtomicInteger(0);
  		AtomicLong popTime = new AtomicLong(0);
  		AtomicInteger pops = new AtomicInteger(0);
    
    // Push logic
    protected boolean tryPush(Node node) {
		Node oldTop = top.get();
		node.next = oldTop;
		return(top.compareAndSet(oldTop, node));
    }
    
    public void push(Integer value) {
    	long StartTime = System.nanoTime();
    	Node node = new Node(value);
		while(true) {
		    if(tryPush(node)) {
		    	pushTime.getAndAdd(System.nanoTime()-StartTime);
				pushs.getAndIncrement();
		    	return;
		    } else {
		    	try {
					backoff.backoff();
				} catch (InterruptedException e) {
					// do nothing
				}
		    }
		}
    }


    // Pop logic
    protected Node tryPop() throws NoSuchElementException {
		Node oldTop = top.get();
		if(oldTop == null) {
		    throw new NoSuchElementException();
		}

		Node newTop = oldTop.next;
		if(top.compareAndSet(oldTop, newTop)) {
		    return oldTop;
		} else {
		    return null;
		}
    }

    public Integer pop() throws NoSuchElementException {
    	long StartTime = System.nanoTime();
		while(true) {
		    Node returnNode = tryPop();
		    if(returnNode != null) {
		    	popTime.getAndAdd(System.nanoTime()-StartTime);
				pops.getAndIncrement();
		    	return returnNode.value;
		    } else {
		    	try {
					backoff.backoff();
				} catch (InterruptedException e) {
					// do nothing
				}
		    }
		}
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

    // Node class
    public class Node {
		public Integer value;
		//public AtomicStampedReference<Node> next;
		public Node next;
		
		public Node(Integer x) {
		    value = x;
		    next = null;
		}
    }
}