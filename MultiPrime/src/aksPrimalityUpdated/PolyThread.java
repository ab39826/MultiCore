package aksPrimalityUpdated;


import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class PolyThread extends Thread{
	BigInteger[] aksPoly;
	int n;
	volatile boolean running=true;
	Random rand = new Random();
	volatile boolean starting = false;
	AtomicLong next;
	BigInteger endIndex;
	
	
	public PolyThread(BigInteger[] aksPoly, int n, AtomicLong nex, BigInteger end){
		this.aksPoly = aksPoly;
		this.n = n;
		next = nex;
		endIndex = end;
	}
	
	public void run(){
		
		while (next.get() < endIndex.longValue()+1){
			long nextK = next.getAndAdd(1);  // Find next coefficient to calculate
			if (nextK > Integer.MAX_VALUE){
				System.out.println("Nuumber out of integer range!");
				System.exit(-1);
			}
			BigInteger big = binomialCoeff(n,(int)nextK);
			
			aksPoly[(int)nextK] = big;
			aksPoly[n-(int)nextK] = big;
			
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