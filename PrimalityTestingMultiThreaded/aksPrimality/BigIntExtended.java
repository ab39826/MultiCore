package aksPrimality;

import java.math.BigInteger;

public class BigIntExtended extends BigInteger
{

	// constructor that takes a primitive int,
	// parses it to a String, and calls the
	// superclass's constructor
	public BigIntExtended(int n)
	{
		super(new Integer(n).toString());
	}

	// constructor that takes a String
	public BigIntExtended(String s)
	{
		super(s);
	}
}