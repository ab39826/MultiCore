package trialDivision.BigInt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Main{
	public static void main(String[] args){
		int numThreads = 0;
		BigInteger highNum;
		//List<BigInteger> primes;// = Collections.synchronizedList(new ArrayList<BigInteger>());
		//List<BigInteger> active;// = Collections.synchronizedList(new ArrayList<BigInteger>());
		//AtomicReference<BigInteger> next;// = new AtomicReference<BigInteger>(new BigInteger("3"));
		//AtomicReference<BigInteger> highest;// = new AtomicReference<BigInteger>(new BigInteger("0"));
		PrimeFindThread[] Threads;
		boolean foundFactor;// = false;
		BigInteger factor;// = new BigInteger("0");
		
		
		int maxThreads = Integer.parseInt(args[0]);
		int numIters = Integer.parseInt(args[1]);
		highNum = new BigInteger(args[2]);
		
		highNum = highNum.nextProbablePrime();
		
		/*AtomicReference<BigInteger> fromLast = new AtomicReference<BigInteger>(highNum);
		if (fromLast.get().mod(new BigInteger("2")).compareTo(new BigInteger("0")) < 1){
			fromLast.set(fromLast.get().subtract(new BigInteger("1")));
		}*/
		
		if(maxThreads < 1 || numIters < 1 || highNum.compareTo(new BigInteger("2")) < 1){
			System.out.println("Input a positive number for max threads, then number of iterations, then the number for which to check primality.");
			System.exit(-1);
		}
		
		numThreads = 6;
		
		BigInteger maxList = PrimeFindThread.BitIntSqrt(highNum);
		
		System.out.println("Checking primality of "+highNum);
		
		for(numThreads=1;numThreads<=maxThreads;numThreads++){
		System.out.println("USING "+numThreads+" THREADS!");
		for (int iter=0;iter<numIters;iter++){
			List<BigInteger> primes = Collections.synchronizedList(new ArrayList<BigInteger>());
			primes.add(new BigInteger("2"));
			List<BigInteger> active = Collections.synchronizedList(new ArrayList<BigInteger>());
			AtomicReference<BigInteger> next = new AtomicReference<BigInteger>(new BigInteger("3"));
			AtomicReference<BigInteger> highest = new AtomicReference<BigInteger>(new BigInteger("0"));
			foundFactor = false;
			factor = new BigInteger("0");
			
			Threads = new PrimeFindThread[numThreads];
		
			long StartTime = System.nanoTime();
		
			primes.add(new BigInteger("2"));
			if(highNum.mod(new BigInteger("2")).compareTo(new BigInteger("0")) == 0){
				foundFactor = true;
				factor = new BigInteger("2");
			}
			else {for(int i=0; i<numThreads; i++){
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
					}else if(Threads[i].getFactor().compareTo(new BigInteger("0")) == 1){
						foundFactor = true;
						factor = Threads[i].getFactor();
						for(int j=0; j<numThreads; j++){
							Threads[j].foundFactor = true;
						}
					}
				}	
			}}
		
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