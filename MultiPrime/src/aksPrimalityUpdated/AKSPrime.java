package aksPrimalityUpdated;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class AKSPrime{
private int numThreads;	
public static boolean verbose = false;	
private static final double LOG2 = Math.log(2.0);
double lg2n;
	
public AKSPrime(int t){
	numThreads = t;
}
	
public boolean aksIsPrime(long n){

	double lg2n = logbase2(n); // Assuming floor
	
	//System.out.println("Step 1, log2 of "+n+" is "+lg2n);
//Step 1 check if n = a^b for any integers a,b >1
	long base = 2;
	long aSquared;
	do
	{
		long result;

		int power = Math.max((int) (lg2n/logbase2(base) - 2),1); // Assuming floor rounding
		int comparison;
		
		do
		{
			power++;
			result = (long)Math.pow((double)base,power);//base.pow(power);
			//comparison = n.compareTo(result);
		}
		while( n > result && power < Integer.MAX_VALUE );
		
		if( n == result )
		{
			return false;
		}
		
		base += 1;
		aSquared = (long)Math.pow((double)base,2);
	}
	while (aSquared <= n);

	
	//System.out.println("Step 2");
//Step 2 of algorithm: Compute smallest r multiplicative order


long logSquared = (long) (lg2n*lg2n);
long k = 1;
long r = 1;
do
{
	r = r+1;
	k = multiplicativeOrder(r, n);
}
while( k < logSquared );
//System.out.println("Check 1");
// If 1 < gcd(a,n) < n for some a <= r, output COMPOSITE
for( long i=0; i<=r; i++ )
{
	long gcd = gcd(n,i);
	if (verbose) System.out.println("gcd(" + n + "," + i + ") = " + gcd);
	if ( gcd>1 && gcd<n )
	{
		return false;
	}
}

//System.out.println("Step 3");
//Step 3 of algorithm: Compute GCD things
long gcd;
long rIndex = r;

while(rIndex > 1){
	gcd = gcd(n,rIndex);
	
	if(gcd > 1 && gcd< n ){
		return false;
	}
		
	rIndex = rIndex-1;
}
//System.out.println("Step 4");
//Step 4 of algorithm


if(n < r){
	return true;
}


//System.out.println("Step 5");
//Step 5 of algorithm
BigInteger totientR = totientNew(new BigInteger(r+""));

BigDecimal totes = new BigDecimal(totientR);
BigDecimal sqrttotes = sqrt(totes,3);

BigDecimal boundDec = sqrttotes.multiply(BigDecimal.valueOf(lg2n));
int bound = boundDec.intValue();

AksPoly polyNorm = new AksPoly((int)n+1);
polyNorm.binomialExpansionThreaded(1, (int)n, numThreads);

for(int j = 1; j<bound; j++){
	AksPoly poly1 = new AksPoly(polyNorm);
	poly1.binomialExpansionThreadedConstant(-1 * j, (int)n, numThreads);
	//System.out.println("mark1");
	
	int [] coeffs2 = {1, (int)n, -1 * j, 0};
	AksPoly poly2 = new AksPoly(coeffs2, (int)n);
	//System.out.println("poly2" +poly2.toString());
	
	poly1.subtract(poly2);
	//System.out.println("poly1" +poly1.toString());
	//System.out.println("mark3");
	
	//if this doesn't hold up for all j's from 1 to the bound, then we know that it's composite
	if(!(poly1.specialMod((int)r, (int)n))){
		return false;
	}
	else{
		//System.out.println("check: j" + j);
	}
}


return true;
		
}

public double logbase2(long n){
	
	return Math.log(n)/Math.log(2);
}

long multiplicativeOrder(long R, long N)
{
	BigInteger k = BigInteger.ZERO;
	BigInteger result;
	
	BigInteger n = new BigInteger(N+"");
	BigInteger r = new BigInteger(R+"");
	
	do
	{
		k = k.add(BigInteger.ONE);
		result = n.modPow(k,r);
	}
	while( result.compareTo(BigInteger.ONE) != 0 && r.compareTo(k) > 0);
	
	if (r.compareTo(k) <= 0){
		return BigInteger.ONE.negate().longValue();
	}
	else
	{
		return k.longValue();
	}
}

public BigInteger totientNew(BigInteger r){

    BigInteger count = BigInteger.ZERO;
    for(int a = 1; a< r.intValue(); a++){
    	if(((r.gcd(BigInteger.valueOf(a)).compareTo(BigInteger.ONE)) == 0)){
  
    		count = count.add(BigInteger.ONE);
    		
    	}
    	
    }
    
    return(count);
    
}

public static long gcd(long a, long b){
	while(a != 0 && b!= 0){
		long c = b;
		b = a%b;
		a = c;
	}
	return a+b;
}

public static BigDecimal sqrt(BigDecimal A, final int SCALE) {
    BigDecimal x0 = new BigDecimal("0");
    BigDecimal x1 = new BigDecimal(Math.sqrt(A.doubleValue()));
    while (!x0.equals(x1)) {
        x0 = x1;
        x1 = A.divide(x0, SCALE, RoundingMode.HALF_UP);
        x1 = x1.add(x0);
        x1 = x1.divide(BigDecimal.valueOf(2), SCALE, RoundingMode.HALF_UP);

    }
    return x1;
}

}