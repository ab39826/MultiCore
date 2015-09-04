package aksPrimality;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class AKSPrime{
private int numThreads;	
public static boolean verbose = false;	
private static final double LOG2 = Math.log(2.0);
BigInteger bigOne = BigInteger.ONE;
BigInteger bigTwo = bigOne.add(bigOne);
double lg2n;
	
public AKSPrime(int t){
	numThreads = t;
}
	
public boolean aksIsPrime(BigInteger n){
	long StartTime = System.nanoTime();
	double lg2n = logbase2(n);
	
//Step 1 check if n = a^b for any integers a,b >1
	BigInteger base = BigInteger.valueOf(2);
	BigInteger aSquared;
	do
	{
		BigInteger result;

		int power = Math.max((int) (lg2n/logbase2(base) - 2),1);
		int comparison;
		
		do
		{
			power++;
			result = base.pow(power);
			comparison = n.compareTo(result);
		}
		while( comparison > 0 && power < Integer.MAX_VALUE );
		
		if( comparison == 0 )
		{
			return false;
		}
		
		base = base.add(BigInteger.ONE);
		aSquared = base.pow(2);
	}
	while (aSquared.compareTo(n) <= 0);

	
	
//Step 2 of algorithm: Compute smallest r multiplicative order


double logSquared = lg2n*lg2n;
BigInteger k = BigInteger.ONE;
BigInteger r = BigInteger.ONE;
do
{
	r = r.add(BigInteger.ONE);
	k = multiplicativeOrder(r, n);
}
while( k.doubleValue() < logSquared );

// If 1 < gcd(a,n) < n for some a <= r, output COMPOSITE
for( BigInteger i = BigInteger.valueOf(2); i.compareTo(r) <= 0; i = i.add(BigInteger.ONE) )
{
	BigInteger gcd = n.gcd(i);
	if (verbose) System.out.println("gcd(" + n + "," + i + ") = " + gcd);
	if ( gcd.compareTo(BigInteger.ONE) > 0 && gcd.compareTo(n) < 0 )
	{
		return false;
	}
}


//Step 3 of algorithm: Compute GCD things
BigInteger gcd;
BigInteger rIndex = r;

while(rIndex.compareTo(BigInteger.ONE) == 1){
	gcd = n.gcd(rIndex);
	
	if(gcd.compareTo(BigInteger.ONE) == 1 && gcd.compareTo(n) == -1 ){
		return false;
	}
		
	rIndex = rIndex.subtract(BigInteger.ONE);
}

//Step 4 of algorithm


if(n.compareTo(r)<1){
	return true;
}



//Step 5 of algorithm
BigInteger totientR = totientNew(r);
BigDecimal totes = new BigDecimal(totientR);
BigDecimal sqrttotes = sqrt(totes,3);



BigDecimal boundDec = sqrttotes.multiply(BigDecimal.valueOf(lg2n));

int bound = boundDec.intValue();
System.out.println("Bound is "+ bound);


AksPoly polyNorm = new AksPoly(n.intValue()+1);
polyNorm.binomialExpansionThreaded(1, n.intValue(), numThreads);

for(int j = 1; j<bound; j++){
	AksPoly poly1 = new AksPoly(polyNorm);
	poly1.binomialExpansionThreadedConstant(-1 * j, n.intValue(), numThreads);
	//System.out.println("mark1");
	
	int [] coeffs2 = {1, n.intValue(), -1 * j, 0};
	AksPoly poly2 = new AksPoly(coeffs2, n.intValue());
	//System.out.println("poly2" +poly2.toString());
	
	poly1.subtract(poly2);
	//System.out.println("mark3");
	
	//if this doesn't hold up for all j's from 1 to the bound, then we know that it's composite
	if(!(poly1.specialMod(r.intValue(), n.intValue()))){
		return false;
	}
	else{
		//System.out.println("check: j" + j);
	}
}

long TotalTime = System.nanoTime() - StartTime;
System.out.println("Total time was "+TotalTime);

return true;
		
}

public double logbase2(BigInteger n){
	
	return ln(n)/ln(bigTwo);
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

double ln(BigInteger x)
{
    int blex = x.bitLength() - 1022; // any value in 60..1023 is ok
    if (blex > 0)
        x = x.shiftRight(blex);
    double res = Math.log(x.doubleValue());
    return blex > 0 ? res + blex * LOG2 : res;
}



BigInteger multiplicativeOrder(BigInteger r, BigInteger n)
{
	BigInteger k = BigInteger.ZERO;
	BigInteger result;
	
	do
	{
		k = k.add(BigInteger.ONE);
		result = n.modPow(k,r);
	}
	while( result.compareTo(BigInteger.ONE) != 0 && r.compareTo(k) > 0);
	
	if (r.compareTo(k) <= 0){
		return BigInteger.ONE.negate();
	}
	else
	{
		return k;
	}
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