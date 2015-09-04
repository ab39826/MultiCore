package q2;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicStampedReference;

public class LockFreeExchangers {
	static final int EMPTY = 0;
	static final int WAITING = 1;
	static final int BUSY = 2;

	AtomicStampedReference<Integer> slot = new AtomicStampedReference<Integer>(
			null, 0);

	public Integer exchange(Integer myItem, long timeout, TimeUnit unit)
			throws TimeoutException {
		long nanos = unit.toNanos(timeout);
		long timeBound = System.nanoTime() + nanos;
		int[] stampHolder = { EMPTY };
		while (true) {
			if (System.nanoTime() > timeBound)
				throw new TimeoutException();
			Integer yrItem = slot.get(stampHolder);
			int stamp = stampHolder[0];
			switch (stamp) {
			case EMPTY:
				if (slot.compareAndSet(yrItem, myItem, EMPTY, WAITING)) {
					while (System.nanoTime() < timeBound) {
						yrItem = slot.get(stampHolder);
						if (stampHolder[0] == BUSY) {
							slot.set(null, EMPTY);
							return yrItem;
						}
					}
					if (slot.compareAndSet(myItem, null, WAITING, EMPTY)) {
						throw new TimeoutException();
					} else {
						yrItem = slot.get(stampHolder);
						slot.set(null, EMPTY);
						return yrItem;
					}
				}
				break;
			case WAITING:
				if (slot.compareAndSet(yrItem, myItem, WAITING, BUSY))
					return yrItem;
				break;
			case BUSY:
				break;
			default: // impossible

			}
		}
	}
}