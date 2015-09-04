package q3goober;

// TODO 
// Implement Fast Mutex Algorithm
public class FastMutexLock implements MyLock 
{
	volatile int x_last;
	volatile boolean[] flag; 
	volatile int y_door;
	int numThreads;
	
	public FastMutexLock(int numThread) 
	{
		int i;
		flag = new boolean[numThread];
		y_door = -1;
		x_last = -1;
		numThreads = numThread;
		
		for(i = 0; i < numThreads; i++)
		{
			flag[i] = false;
		}
    }
	
    @Override
    public void lock(int myId) 
    {
    	while(true)
    	{
    		flag[myId] = true;
    		x_last = myId;
    		
    		/* Left */
    		if(y_door != -1)
    		{
    			flag[myId] = false;
    			while(y_door != -1);
    			continue;
    		}
    		else
    		{
    			y_door = myId;
    			
    			/* Down */
    			if(x_last == myId)
    				return;
    			/* Right */
    			else
    			{
    				int i;
    				
    				flag[myId] = false;
    				for(i = 0; i < numThreads; i++)
    					while((i != myId) && flag[i]);
    				if(y_door == myId)
    					return;
    				else
    				{
    					while(y_door != -1);
    					continue;
    				}
    			}
    		}
    	}
    }

    @Override
    public void unlock(int myId)
    {
    	y_door = -1;
    	flag[myId] = false;
    }
}
