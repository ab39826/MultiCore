package q2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class UnboundedLockFreeQueue implements iQueue {

	// used for profiling throughput of the application
	AtomicLong enqTime = new AtomicLong(0);
	AtomicInteger enqs = new AtomicInteger(0);
	AtomicLong deqTime = new AtomicLong(0);
	AtomicInteger deqs = new AtomicInteger(0);

	private final Node blank = new Node(null, null);
	private final AtomicReference<Node> head = new AtomicReference<Node>(blank);
	private final AtomicReference<Node> tail = new AtomicReference<Node>(blank);

	public UnboundedLockFreeQueue() {

	}

	public void enqueue(Integer x) {
		long StartTime = System.nanoTime();

		Node e = new Node(x, null);

		while (true) {
			Node currentTail = tail.get();
			Node nextTail = currentTail.next.get();

			if (currentTail == tail.get()) {
				if (nextTail != null) {
					tail.compareAndSet(currentTail, nextTail);
				} else {

					if (currentTail.next.compareAndSet(null, e)) {
						enqs.getAndIncrement();
						enqTime.getAndAdd(System.nanoTime() - StartTime);
						tail.compareAndSet(currentTail, e);
						return;
					}
				}
			}
		}
	}

	public Integer dequeue() {

		long StartTime = System.nanoTime();

		for (;;) {
			Node oldHead = head.get();
			Node oldTail = tail.get();

			Node oldHeadNext = oldHead.next.get();

			if (oldHead == head.get()) {
				if (oldHead == oldTail) {
					if (oldHeadNext == null) {
						deqTime.getAndAdd(System.nanoTime() - StartTime);
						return null;
					}

					tail.compareAndSet(oldTail, oldHeadNext);
				} else {
					if (head.compareAndSet(oldHead, oldHeadNext)) {
						deqs.getAndIncrement();
						deqTime.getAndAdd(System.nanoTime() - StartTime);
						return oldHeadNext.value;
					}
				}
			}
		}
	}

	public long getEnqTime() {
		return enqTime.get();
	}

	public long getDeqTime() {
		return deqTime.get();
	}

	public int getEnqs() {
		return enqs.get();
	}

	public int getDeqs() {
		return deqs.get();
	}

	private static class Node {
		final Integer value;
		final AtomicReference<Node> next;

		public Node(Integer x, Node nextNode) {
			value = x;
			next = new AtomicReference<Node>(null);
		}

	}
}