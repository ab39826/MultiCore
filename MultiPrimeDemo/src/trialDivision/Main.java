package trialDivision;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Main{
	public static void main(String[] args){
		long highNum;
		PrimeFindThread[] Threads;
		boolean foundFactor;
		long factor;
		
		int maxThreads = Integer.parseInt(args[0]);
		int numIters = Integer.parseInt(args[1]);
		highNum = Long.parseLong(args[2]);
		
		if(maxThreads < 1 || numIters < 1 || highNum <= 2){
			System.out.println("Input a positive number for max threads, then number of iterations, then the number for which to check primality.");
			System.exit(-1);
		}
		
		long maxList = (long) Math.sqrt((double)highNum);
		
		System.out.println("Checking primality of "+highNum);
		
		for(int numThreads=1;numThreads<=maxThreads;numThreads++){
		System.out.println("USING "+numThreads+" THREADS!");
		for (int iter=0;iter<numIters;iter++){
			List<Long> primes = Collections.synchronizedList(new ArrayList<Long>());
			primes.add((long)2);
			List<Long> active = Collections.synchronizedList(new ArrayList<Long>());
			AtomicLong next = new AtomicLong(3);
			AtomicLong highest = new AtomicLong(0);
			foundFactor = false;
			factor = 0;
			
			Threads = new PrimeFindThread[numThreads];
		
			long StartTime = System.nanoTime();
			
			if(highNum % 2 == 0){
				foundFactor = true;
				factor = 2;
			}
			else {
				for(int i=0; i<numThreads; i++){
					Threads[i] = new PrimeFindThread(highNum, maxList);
					Threads[i].primes = primes;
					Threads[i].active = active;
					Threads[i].next = next;
					Threads[i].highest = highest;
					Threads[i].threadNum = i;
					Threads[i].start();
				}
				boolean going = true;
				while (going){
					going = false;
					for(int i=0; i<numThreads; i++){
						if(Threads[i].isGoing()){
							going = true;
						}else if(Threads[i].getFactor() > 1){
							foundFactor = true;
							factor = Threads[i].getFactor();
							for(int j=0; j<numThreads; j++){
								Threads[j].foundFactor = true;
							}
						}
					}	
				}
			}
		
			long TotalTime = System.nanoTime() - StartTime;

			if(foundFactor){
				System.out.println("The number "+highNum+" is divisible by "+factor+", so it is not prime.");
			} else{
				System.out.println("The number "+highNum+" is prime.");
			}
			System.out.println("Total time was "+TotalTime);
		}
		}
	}
	
	
	
}