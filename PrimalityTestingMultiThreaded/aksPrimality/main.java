package aksPrimality;

public class main{
	
	public static void main(String[] args){
		
		int maxThreads = Integer.parseInt(args[0]);
		
		int numIters = Integer.parseInt(args[1]);

		long n = Long.parseLong(args[2]);
		
		for(int numThreads=1;numThreads<=maxThreads;numThreads++){
			for(int i=0;i<numIters;i++){
			AKSPrime aks = new AKSPrime(numThreads);
			System.out.println("Using "+numThreads+" threads");
			System.out.println("Checking primality of "+n);
			long StartTime = System.nanoTime();
			if(aks.aksIsPrime(n)){
				System.out.println("Total time was "+(System.nanoTime()-StartTime));
				System.out.println(""+ n+ " is prime!");
			}
			else{
				System.out.println("Total time was "+(System.nanoTime()-StartTime));
				System.out.println("" + n+ " is composite");
			}
			}
		}
	}
	
}