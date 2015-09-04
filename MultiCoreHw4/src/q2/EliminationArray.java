package q2;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class EliminationArray {
	private static final int duration = 5;
	LockFreeExchangers[] exchanger;

	Random random;

	public EliminationArray(int capacity) {
		exchanger = (LockFreeExchangers[]) new LockFreeExchangers[capacity];
		for (int i = 0; i < capacity; i++) {
			exchanger[i] = new LockFreeExchangers();
		}
		random = new Random();
	}

	public Integer visit(Integer value, int range) throws TimeoutException {
		int slot = random.nextInt(range);
		return (exchanger[slot]
				.exchange(value, duration, TimeUnit.MILLISECONDS));
	}
}