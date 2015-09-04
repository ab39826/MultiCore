package aksPrimalityUpdated;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class constThread extends Thread{
	BigInteger[] aksPoly;
	int n;
	BigInteger a;
	volatile boolean running=true;
	AtomicLong next;
	BigInteger endIndex;
	
	public constThread(BigInteger[] aksPoly, int n, int a, AtomicLong nex, BigInteger end){
		this.aksPoly = aksPoly;
		this.n = n;
		this.a = new BigIntExtended(a);
		next = nex;
		endIndex = end;
	}
	
	public void run(){
		
		while (next.get() < endIndex.longValue()+1){
			long nextK = next.getAndAdd(1);  // Find next coefficient to calculate
			
			if (nextK > Integer.MAX_VALUE || nextK < Integer.MIN_VALUE){
				System.out.println("Nuumber out of integer range!");
				System.exit(-1);
			}
			//System.out.println("nextK is"+nextK);
			if((int)nextK<=n){
				aksPoly[(int)nextK] = a.pow((int)nextK);
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