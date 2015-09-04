package aksPrimality;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;


public class AksPoly
{
	// instance variables
	private BigInteger [] poly;
	private int degree;
	private int polySize;
	private static final int defaultPolySize = 100;
	int numThreads;


	// constructor
	public AksPoly()
	{
		polySize = defaultPolySize;
		poly = new BigInteger[polySize];
		degree = 0;
	}

	// constructor that takes size as input
	public AksPoly(int size)
	{
		poly = new BigInteger[size];
		degree = 0;
		polySize = size;
	}
	
	public AksPoly(AksPoly other){
		poly = new BigInteger[other.degree + 1];
		degree = other.degree;
		polySize = other.polySize;
		
		for(int i = 0; i<=other.degree; i++){
			poly[i] = other.poly[i];
			
		}
		
	}

	// constructor that takes data as input
	public AksPoly(int[] data, int n)
	{
		poly = new BigInteger[n+1];


		for(int ctr = 1; ctr < data.length; ctr = ctr + 2)
		{
			if(data[ctr] > degree){
				degree = data[ctr];
			}
			
			poly[data[ctr]] = new BigIntExtended(data[ctr - 1]);
		}
	}

	
	public String toString()
	{
		String polyString = "";

		if(degree != 0)
		{
			for(int i = 0; i <= degree; i++)
			{
				if(poly[i] != null && !poly[i].equals(BigInteger.ZERO))
				{
					if( i<degree){
					polyString += "" + poly[i] + "*x^" + i + " + ";
					}
					else{
						polyString += "" + poly[i] + "*x^" + i;
					}
				}
			}
		}
		else{
			polyString += "0";
		}
		return polyString;
	}

	// This method adds two AksPolys
	public void add(AksPoly otherPoly)
	{
		if(otherPoly.getDegree() > degree)
		{
			degree = otherPoly.getDegree();
			BigInteger [] temp = new BigInteger [degree + 1];

			
			for(int i = 0; i <= degree; i++)
			{
				if(!this.getCoeffAtIndex(i).equals(BigIntExtended.ZERO))
				{
					if(temp[i] != null){
						temp[i] = temp[i].add(this.getCoeffAtIndex(i));
						}
					else{temp[i] = this.getCoeffAtIndex(i);}
				}
			}

			for(int i = 0; i <= degree; i++)
			{
				if(!otherPoly.getCoeffAtIndex(i).equals(BigIntExtended.ZERO))
				{
						temp[i] = otherPoly.getCoeffAtIndex(i);
				}
			}

			poly = temp;
		}

		for(int i = 0; i <= otherPoly.getDegree(); i++)
		{
			if(!(otherPoly.getCoeffAtIndex(i).equals(BigIntExtended.ZERO)))
			{
				this.addCoeffAtIndex(i, otherPoly.getCoeffAtIndex(i));
			}
		}
	}

	// This method subtracts p from this polynomial
	public void subtract(AksPoly otherPoly)
	{
		otherPoly.multiplyByConstant(-1);
		this.add(otherPoly);
	}

	// This method multiplies the coefficients of this polynomial
	// by a constant c
	public void multiplyByConstant(int factor)
	{
		for(int i = 0; i <= degree; i++)
		{
			if(poly[i] != null)
				poly[i] = poly[i].multiply(new BigIntExtended(factor));
		}

	}
	

	public void multiply(AksPoly p)
	{
		BigInteger [] poly2 = p.getPoly();
		int newDegree = this.getDegree() + p.getDegree();

		BigInteger [] polytemp = new BigInteger[newDegree + 1];

		for(int i = 0; i < poly.length; i++)
		{
			for(int j = 0; j < poly2.length; j++)
			{
				BigInteger t = poly[i].multiply(poly[j]);
				if(polytemp[i + j] != null)
					polytemp[i + j].add(t);
				else
				{
					polytemp[i + j] = new BigInteger("0");
					polytemp[i + j].add(t);
				}
			}
		}

		poly = polytemp;
		degree = newDegree;
	}
	
	// This private helper method multiplies two polynomials
		private BigInteger[] multiply(BigInteger[] polya, int degree1, BigInteger[] polyb, int degree2)
		{
		
			int newDegree = degree1 + degree2;

			BigInteger [] polytemp = new BigInteger[newDegree + 1];

			for(int i = 0; i <= degree1; i++)
			{
				for(int j = 0; j <= degree2; j++)
				{
					if(polya[i] != null && polyb[j] != null)
					{
						BigInteger value = polya[i].multiply(polyb[j]);

						if(polytemp[i + j] != null)
							polytemp[i + j] = polytemp[i + j].add(value);
						else
						{
							polytemp[i + j] = new BigInteger("0");
							polytemp[i + j] = polytemp[i + j].add(value);
						}
					}
				}
			}

			return polytemp;
		}

	// makes this Polynomial into a polynomial
	// representing (x + a)^n, using an algorithm of
	// successive squaring
	//Works as follows, take the power you're raising the polynomial seed to (n)
	//convert it to a binary sequence and do succesive squarings and multiplies by (a+x)^1
	public void binomialExpansionViaSquaring(int a, int n)
	{
		
		//this represents the polynomial that you're building up
		poly = new BigInteger[n + 1];
		BigInteger[] buildPoly = new BigInteger[n + 1];
		buildPoly[0] = new BigIntExtended(a);
		buildPoly[1] = new BigIntExtended(1);
		int buildDegree = 1;

		//this represents a non-exponentiated polynomial seed.
		BigInteger[] seedPoly = new BigInteger[n + 1];
		seedPoly[0] = new BigIntExtended(a);
		seedPoly[1] = new BigIntExtended(1);
		int newDegree = 1;

		String binaryN = Integer.toBinaryString(n);

		char c;

		buildPoly = multiply(buildPoly, buildDegree, buildPoly, buildDegree);

		buildDegree = 2*buildDegree;

		for(int index = 1; index < binaryN.length(); index++)
		{
			
			//first check to see each bit of sequence to see whether you need to multiply seed
			c = binaryN.charAt(index);
			if(c == '1')
			{
				buildPoly = multiply(buildPoly, buildDegree, seedPoly, newDegree);
				buildDegree = buildDegree + 1;
			}

			if(index != binaryN.length() - 1)
			{
				buildPoly = multiply(buildPoly, buildDegree, buildPoly, buildDegree);
				buildDegree = 2*buildDegree;
			}

		}

		poly = buildPoly;
		degree = buildDegree;
	}

	
	public void binomialExpansion(int a, int n)
	{
		poly = new BigInteger[n + 1];
		BigInteger temp = new BigIntExtended("0");
		

		for(int k = 0; k <= n; k++)
		{

			temp = binomialCoeff(n,k);

			poly[n - k] = temp;
		}

		degree = n;
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
		
		public void binomialExpansionThreaded(int a, int n, int nT){
		
			AtomicLong next = new AtomicLong(0);
			
			numThreads = nT;
			
			
			PolyThread[] Threads;
			Threads = new PolyThread[numThreads];
			
			
			poly = new BigInteger[n+1];
	
			
			
			int end = (n/2);
			
			if((n+1)%2 == 1){
				end++;
			}
			
			BigInteger endIndex = BigInteger.valueOf(end);
			
		
			for(int i=0;i<numThreads;i++){
				Threads[i] = new PolyThread(poly, n, next, endIndex );
				Threads[i].start();
			}
			
			
			
			//Now wait to compute all binomial coefficients
			boolean going = true;
			while (going){
				going = false;
				for(int i=0;i<numThreads;i++){
					if(Threads[i].isRunning()){
						going = true;
					}
				}
			}
					
			degree = n;
			
		}
		
		public void binomialExpansionThreadedConstant(int a, int n, int nT){
			
			AtomicLong next = new AtomicLong(0);
			
			numThreads = nT;
			
						
			constThread[] Threads;
			Threads = new constThread[numThreads];
			
			BigInteger[] constantArray = new BigInteger[n+1];
			
			for(int i = 0; i<numThreads; i++){
			Threads[i] = new constThread(constantArray, n, a, next, BigInteger.valueOf(n));
			Threads[i].start();
			}
				
			//wait to compute all a^n coefficients
			for(int i = 0; i<numThreads; i++){
				try {
					Threads[i].join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//after this is done, need to elementwise multiply each element of poly array with 
			//aconstantarray to get your thing
			for(int i = 0; i<n; i++){
				if(constantArray[n-i] == null){
					constantArray[n-i] = BigInteger.valueOf(a).pow((n-i));
				}
				poly[i] = poly[i].multiply(constantArray[n-i]);
			}
			
			degree = n;
			
		}
		
		
	// This method checks to see if this Polynomial
	// is congruent to zero mod (x^r - 1, n)
	public boolean specialMod(int r, int n)
	{
		BigInteger total;
		boolean mod = true;

		for(int start = 0; start < r; start++)
		{

			total = new BigIntExtended("0");

			for(int x = start; x < this.getDegree(); x = x + r)
			{
				BigInteger temp = poly[x];
				total = total.add(temp);
			}

			BigIntExtended bigN = new BigIntExtended(n);

			if(!total.mod(bigN).equals(BigIntExtended.ZERO))
				mod = false;
		}

		return mod;
	}

	public BigInteger[] getPoly()
	{
		return poly;
	}


	public int getDegree()
	{
		return degree;
	}

	//returns given coefficent for power index
	public BigInteger getCoeffAtIndex(int index)
	{
		if(poly[index] != null){
			return poly[index];
		}
		else{return BigInteger.ZERO;}
	}

	// This method adds a to the coefficient at the given index
	public void addCoeffAtIndex(int index, BigInteger a)
	{
		if(poly[index] != null){
			poly[index] = poly[index].add(a);
			}
		
		else{poly[index] = a;}
	}

}