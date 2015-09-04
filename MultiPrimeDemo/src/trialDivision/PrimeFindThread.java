package trialDivision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.lang.Math;

public class PrimeFindThread extends Thread{
	List<Long> primes;
	AtomicLong next;
	AtomicLong highest;
	List<Long> active;
	Iterator<Long> primeIter;
	long highNum;
	long maxList;
	boolean going = true;
	boolean foundFactor = false;
	long factor = 0;
	int threadNum;
	
	public PrimeFindThread(long hiNum, long mList){
		highNum = hiNum;
		maxList = mList;
	}
	
	public void run(){
		
		while (next.get()<= maxList && !foundFactor){
			long num = next.getAndAdd((long)2);  // get and add two to current count
			if(active.contains(num)){
				System.out.println("OH NO!");
			}
			active.add(num);

			long Numsqrt = (long) Math.sqrt((double)num);
			boolean waiting = true;
			while(waiting){
				waiting = false;
				List<Long> current = new ArrayList<Long>(active);
				Iterator<Long> a = current.listIterator();
				while(a.hasNext()){
					// If any numbers less than the square root of this number 
					// are still being checked, we must wait
					if(a.next() <= Numsqrt){
						waiting = true;
					}
				}
				
			}
			
			// Must copy list of primes so far to check this number for primality
			// Otherwise, other threads could add while the list is being iterated through
			// The preceding block checks that there are no required numbers that are still being checked
			List<Long> myPrimes = new ArrayList<Long>(primes);
			boolean prime = true;
			Iterator<Long> p = myPrimes.listIterator();
			while(prime && p.hasNext()){ // Factor has not yet been found and more prime factors to try
				long n = p.next();
				// If next is less than the square root of the current number and the mod is 0
				if(n <= Numsqrt  && num%n == 0){
					prime = false;
				}
			}
			if(prime == true){
				primes.add(num);
				if(highNum%num == 0){
					prime = false;
					foundFactor = true;
					factor = num;
				}
			}
			active.remove((Long)num);
		}
		
		going = false;
		
	}
	
	public boolean isGoing(){
		return going;
	}
	
	public long getFactor(){
		return factor;
	}
}