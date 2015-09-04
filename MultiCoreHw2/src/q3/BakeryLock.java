package q3;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

// TODO
// Implement the bakery algorithm

public class BakeryLock implements MyLock {

	private int numThread;
	ConcurrentHashMap<Integer, Boolean> flag;
	ConcurrentHashMap<Integer, Integer> label;
//	boolean[] flag;
//	int[] label;
	
	
	public BakeryLock(int numThread) {
		this.numThread = numThread;
		flag = new ConcurrentHashMap<Integer, Boolean>();
		label = new ConcurrentHashMap<Integer, Integer>();
//    	flag = new boolean[numThread];
//    	label = new int[numThread];
	}

	@Override
	public void lock(int myId) {
		flag.put(myId, true);
		label.put(myId, max() + 1);
//		System.out.println(label.get(myId));
//		flag[myId] = true;
//		label[myId] = max(label) + 1;
//		System.out.println(label[myId]);
//		System.out.println(label.get(myId));
		while (wait(myId)) {
		}
	}

	@Override
	public void unlock(int myId) {
		flag.put(myId, false);
//		flag[myId] = false;
	}

	private int max() {
		Set<Integer> hashSet = label.keySet();
		if (hashSet.isEmpty()) 
			return 0;
		else
			return Collections.max(label.values());
	}
	
//	private int max(int[] label) {
////		Set<Integer> hashSet = label.keySet();
////		Iterator<Integer> iterator = hashSet.iterator();
//		int max = 0;
//
////		while (iterator.hasNext()) {
////			int next = label.get(iterator.next());
////			if (max < next)
////				max = next;
////		}
//		
//		for (int x : label) {
//			if (max < x) 
//				max = x;
//		}
//
//		return max;
//	}

	private boolean wait(int myId) {
		Set<Integer> hashSet = label.keySet();
		Iterator<Integer> iterator = hashSet.iterator();

		while (iterator.hasNext()) {
			int current = iterator.next();
			if (flag.get(current)
					&& ((label.get(current) < label.get(myId)) || ((label
							.get(current) == label.get(myId)) && current < myId))) {
				return true;
			}
		}

		return false;
	}
	
//	private boolean wait(int[] label,
//			boolean[] flag, int myId) {
////		Set<Integer> hashSet = label.keySet();
////		Iterator<Integer> iterator = hashSet.iterator();
//
////		while (iterator.hasNext()) {
////			int current = iterator.next();
////			if (flag.get(current)
////					&& ((label.get(current) < label.get(myId) || ((label
////							.get(current) == label.get(myId)) && current < myId)) {
////				return true;
////			}
////		}
//		
//		for (int i = 0; i < numThread; i++) {
//			if (flag[i] && ((label[i] < label[myId])) || ((label[i] == label[myId]) && i < myId)) {
//				return true;
//			}
//		}
//
//		return false;
//	}

}
