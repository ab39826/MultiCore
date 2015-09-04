package q4;

import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PSearch implements Callable<Integer> {
	// Declare variables or constructors here;
	// however, they will not be access by TA's test drvier.

	int[] A;
	int begin;
	int end;
	int x;

	public PSearch(int[] A, int begin, int end, int x) {
		this.A = A;
		this.begin = begin;
		this.end = end;
		this.x = x;
	}

	public static int parallelSearch(int x, int[] A, int numThreads) {
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		
		int start = 0;
		int partitionSize = A.length / numThreads;
		int finish = start + partitionSize;
		
		for (int i = 0; i < numThreads - 1; i++) {
			Callable<Integer> callable = new PSearch(A, start, finish,  x);
			Future<Integer> future = executor.submit(callable);
			list.add(future);
			start = finish;
			finish = finish + partitionSize;
		}

		Callable<Integer> callable = new PSearch(A, start, A.length,  x);
		Future<Integer> future = executor.submit(callable);
		list.add(future);
		
		executor.shutdown();
				
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		for (Future<Integer> f : list) {
			try {
				if (f.get() != -1) {
					return f.get();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return -1; // return -1 if the target is not found
	}

	public Integer call() throws Exception {
		// your algorithm needs to use this method to get results
		for (int i = begin; i < end; i++) {
			if (A[i] == x)
				return i;
		}
		return Integer.valueOf(-1);
	}

	public static void main(String args[]) {
		int [] A = {5, 7, 1, 3, 9, 2, 10, 1, 6, 0, 13, 17, 200, 298, 34, 75, 92, 10, 10};
		System.out.println(PSearch.parallelSearch(10, A, 10));
	}
}
