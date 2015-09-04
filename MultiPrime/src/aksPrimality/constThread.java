package aksPrimality;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class constThread extends Thread{
	BigInteger[] aksPoly;
	int n;
	BigInteger a;
	volatile boolean running=true;
	AtomicReference<BigInteger> next;
	BigInteger endIndex;
	
	public constThread(BigInteger[] aksPoly, int n, int a, AtomicReference<BigInteger> nex, BigInteger end){
		this.aksPoly = aksPoly;
		this.n = n;
		this.a = new BigIntExtended(a);
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
			
			if(nextK.intValue()<=n){
			aksPoly[nextK.intValue()] = a.pow(nextK.intValue());
			}
			else{
				aksPoly[n] = a.pow(n);
			}
		}
		
		
		running = false; 
		
	}
	
	public boolean isRunning(){
		return running;
	}
}