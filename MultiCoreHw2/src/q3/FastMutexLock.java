package q3;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// TODO 
// Implement Fast Mutex Algorithm
public class FastMutexLock implements MyLock {

	private int numThread;
	private int x;
	private int y;
	ConcurrentHashMap<Integer, Boolean> flag;

	public FastMutexLock(int numThread) {
		this.numThread = numThread;
		flag = new ConcurrentHashMap<Integer, Boolean>();
		x = -1;
		y = -1;
	}

	@Override
	public void lock(int myId) {
		while (true) {
			flag.put(myId, true);
			x = myId;
			if (y != -1) {
				while (y != -1) {
					flag.put(myId, false);
				}
			} else {
				y = myId;
				if (x == myId) {return;}
				else {
					flag.put(myId, false);
					while (wait(flag)) {
					}
					if (y == myId) {
						return;
					} else {
						while (y != -1) {
						}
					}
				}
			}
		}
	}

	@Override
	public void unlock(int myId) {
		y = -1;
		flag.put(myId, false);
	}

	private boolean wait(ConcurrentHashMap<Integer, Boolean> flag) {
		Set<Integer> hashSet = flag.keySet();
		Iterator<Integer> iterator = hashSet.iterator();

		while (iterator.hasNext()) {
			int current = iterator.next();
			if (flag.get(current) == true) {
				return true;
			}
		}

		return false;

	}
}
