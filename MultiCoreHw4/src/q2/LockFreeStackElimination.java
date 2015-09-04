package q2;

import java.util.concurrent.TimeoutException;

public class LockFreeStackElimination extends LockFreeStackBackoff {
	static final int capacity = 100;
	EliminationArray eliminationArray = new EliminationArray(capacity);

	static ThreadLocal<RangePolicy> policy = new ThreadLocal<RangePolicy>() {
		protected synchronized RangePolicy initialValue() {
			return new RangePolicy(20);
		}
	};

	public void push(Integer value) {
		long StartTime = System.nanoTime();
		RangePolicy rangePolicy = policy.get();
		Node node = new Node(value);
		while (true) {
			if (tryPush(node)) {
				pushTime.getAndAdd(System.nanoTime()-StartTime);
				pushs.getAndIncrement();
				return;
			} else
				try {
					Integer otherValue = eliminationArray.visit(value,
							rangePolicy.getRange());
					if (otherValue == null) {
						rangePolicy.recordEliminationSuccess();
						pushTime.getAndAdd(System.nanoTime()-StartTime);
						pushs.getAndIncrement();
						return; // exchanged with pop
					}
				} catch (TimeoutException ex) {
					rangePolicy.recordEliminationTimeout();
				}
		}
	}

	public Integer pop() {
		long StartTime = System.nanoTime();
		RangePolicy rangePolicy = policy.get();
		while (true) {
			Node returnNode = tryPop();
			if (returnNode != null) {
				popTime.getAndAdd(System.nanoTime()-StartTime);
				pops.getAndIncrement();
				return returnNode.value;
			} else
				try {
					Integer otherValue = eliminationArray.visit(null,
							rangePolicy.getRange());
					if (otherValue != null) {
						rangePolicy.recordEliminationSuccess();
						popTime.getAndAdd(System.nanoTime()-StartTime);
						pops.getAndIncrement();
						return otherValue;
					}
				} catch (TimeoutException ex) {
					rangePolicy.recordEliminationTimeout();
				}
		}
	}

}