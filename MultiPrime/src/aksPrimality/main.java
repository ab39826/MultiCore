package aksPrimality;

import java.math.BigInteger;

public class main{
	
	public static void main(String[] args){
		int numThreads = 1;
		
		AKSPrime aks = new AKSPrime(numThreads);
		
		String number = "619";
		BigInteger n = new BigInteger(number);
		
	
		
		if(aks.aksIsPrime(n)){
			System.out.println(""+ n+ " is prime!");
		}
		else{
			System.out.println("" + n+ " is composite");
		}
		
		System.out.println("Done!");
	}
	
}