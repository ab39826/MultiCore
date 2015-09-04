package aksPrimality;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class PolyThread extends Thread{
	BigInteger[] aksPoly;
	int n;
	volatile boolean running=true;
	Random rand = new Random();
	volatile boolean starting = false;
	AtomicReference<BigInteger> next;
	BigInteger endIndex;
	
	
	public PolyThread(BigInteger[] aksPoly, int n, AtomicReference<BigInteger> nex, BigInteger end){
		this.aksPoly = aksPoly;
		this.n = n;
		next = nex;
		endIndex = end;
	}
	
	public void run(){
		
		while (next.get().compareTo(endIndex) < 1){
			BigInteger nextK = next.get().add(BigInteger.ONE);  // Find next number to check for primality
			BigInteger num = next.getAndSet(nextK);  // get and set current number
			// While another thread messed up the counting between the last two lines
			while(num.compareTo(new BigInteger(nextK+"").subtract(BigInteger.ONE)) != 0){ 
				nextK = next.get().add(BigInteger.ONE);  // Try again with new number
				num = next.getAndSet(nextK);
			}
			
			BigInteger big = binomialCoeff(n,nextK.intValue());
			
			aksPoly[nextK.intValue()] = big;
			aksPoly[n-nextK.intValue()] = big;
			
		}
		
		running = false; 			
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public BigInteger binomialCoeff(int n, int k){
		
		
		BigIntExtended numVal;
		BigInteger numerator = new BigIntExtended("1");
		
		BigIntExtended denVal;
		BigInteger denominator = new BigIntExtended("1");
		
		for(int i = 1; i<=k; i++)
		{
			numVal = new BigIntExtended(n+1-i);
			numerator = numerator.multiply(numVal);
			
			denVal = new BigIntExtended(i);
			denominator = denominator.multiply(denVal);	
		}
	
		numerator = numerator.divide(denominator);
		
		return numerator;
	}
}