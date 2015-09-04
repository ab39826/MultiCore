package trialDivision.BigInt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class PrimeFindThread extends Thread{
	List<BigInteger> primes;
	AtomicReference<BigInteger> next;
	AtomicReference<BigInteger> highest;
	List<BigInteger> active;
	Iterator<BigInteger> primeIter;
	BigInteger highNum;
	BigInteger maxList;
	BigInteger one = new BigInteger("1");
	BigInteger two = new BigInteger("2");
	boolean going = true;
	boolean foundFactor = false;
	BigInteger factor = new BigInteger("0");
	//boolean checking = false;
	int threadNum;
	
	public PrimeFindThread(BigInteger hiNum, BigInteger mList){
		highNum = hiNum;
		maxList = mList;
	}
	
	public void run(){
		
		while (next.get().compareTo(maxList) < 1 && !foundFactor){
			BigInteger nextNum = next.get().add(two);  // Find next number to check for primality
			BigInteger num = next.getAndSet(nextNum);  // get and set current number
			// While another thread messed up the count between the last two lines
			// One thread must have added two, thus the old would be equal to the new minus two for that thread
			while(num.compareTo(new BigInteger(nextNum+"").subtract(two)) != 0){ 
				if(num.compareTo(new BigInteger(nextNum+"")) == 1){
					System.out.println("NEEXXT");
					nextNum = num;
					num = next.getAndSet(num); 
				}
				nextNum = next.get().add(two);  // Try again with new number
				num = next.getAndSet(nextNum);
			}
			if(active.contains(num)|| primes.contains(num)){
				System.out.println(num+" is already worked on!");
			}
			active.add(num);

			BigInteger Numsqrt = BitIntSqrt(num);
			boolean waiting = true;
			while(waiting){
				waiting = false;
				List<BigInteger> current = new ArrayList<BigInteger>(active);
				Iterator<BigInteger> a = current.listIterator();
				while(a.hasNext()){
					// If any numbers less than the square root of this number 
					// are still being checked, we must wait
					if(a.next().compareTo(Numsqrt) < 1){
						waiting = true;
					}
				}
				
			}
			
			// Must copy list of primes so far to check this number for primality
			// Otherwise, other threads could add while the list is being iterated through
			// The preceding block checks that there are no required numbers that are still being checked
			List<BigInteger> myPrimes = new ArrayList<BigInteger>(primes);
			boolean prime = true;
			Iterator<BigInteger> p = myPrimes.listIterator();
			while(prime && p.hasNext()){ // Factor has not yet been found and more prime factors to try
				BigInteger n = p.next();
				// If next is less than the square root of the current number and the mod is 0
				if(n.compareTo(Numsqrt) < 1  && num.mod(n).compareTo(new BigInteger("0")) == 0){
					prime = false;
				}
			}
			if(prime == true){
				primes.add(num);
				if(highNum.mod(num).compareTo(new BigInteger("0")) == 0){
					prime = false;
					foundFactor = true;
					factor = num;
					//System.out.println("Thread "+threadNum+" found factor: "+factor);
				}
			}
			active.remove(num);
		}
		
		going = false;
		
	}
	
	public boolean isGoing(){
		return going;
	}
	
	public BigInteger getFactor(){
		return factor;
	}
	
	public static BigInteger BitIntSqrt(BigInteger n){
		BigDecimal numBya, numByaPlusa, numByaPlusaHalf;
		BigDecimal result = new BigDecimal("1");
		BigDecimal n2 = new BigDecimal("2");
		BigDecimal num = new BigDecimal(n+"");
		do{
			numBya = num.divide(result,9,BigDecimal.ROUND_FLOOR);
			numByaPlusa = numBya.add(result);
			numByaPlusaHalf = numByaPlusa.divide(n2,9,BigDecimal.ROUND_FLOOR);
			result = numByaPlusaHalf;
			//System.out.println("Comparing "+result.pow(2)+" to "+num);
		}while(result.pow(2).compareTo(num)==1); //&& result.add(new BigDecimal("1")).pow(2).compareTo(num)==1);
		
		return result.toBigInteger();
		
	}
	
}