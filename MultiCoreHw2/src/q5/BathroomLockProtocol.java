package q5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// TODO
// Use locks and condition variables to implement the bathroom protocol
public class BathroomLockProtocol implements Protocol {
	// declare the lock and conditions here
	//Lock femaleLock = new ReentrantLock(true);
	//Lock maleLock = new ReentrantLock(true);
	Lock lock = new ReentrantLock(true);
	Condition condition = lock.newCondition();
	//Condition femaleCond = femaleLock.newCondition();
	//Condition maleCond = maleLock.newCondition();
	
	//"Enters" variables keep track of how many lock acquires for females and male threads respectively
	
	//"Leaves" variables keep track of how many lock relinquishes happen for fem/male respectively
	
	volatile int femaleEnters = 0;
	volatile int femaleLeaves = 0;
	volatile int maleEnters = 0;
	volatile int maleLeaves = 0;
	
	/*
	"Knocks" variables are used to signal that (For example) a girl thread 
	would like to enter the  bathroom and so any boys that are not already in the bathroom
	but would like to come in MUST wait for the girls that have begun queueing up to enter
	the bathroom
	 
	 */
	
	boolean femaleKnocks = false;
	boolean maleKnocks = false;
	
	/*
	 Notes:
	 
	 enter function
	 */
	
	public void enterMale() {
		
		lock.lock();
		try{
			while(femaleKnocks){
				maleKnocks = true;
				try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//should fall into the next loop
			maleKnocks = true;
			while(femaleEnters != femaleLeaves) {
				try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			maleEnters++;
			maleKnocks =false;
		}
		finally{
			lock.unlock();
		}
	}

	public void leaveMale() {
		lock.lock();
		try{
			maleLeaves++;
			if(maleEnters == maleLeaves){
				condition.signalAll();
			}
		}
		finally{
			lock.unlock();
		}
	}

	public void enterFemale() {
		lock.lock();
		try{
			while(maleKnocks) {
				femaleKnocks = true;
				try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			femaleKnocks = true;
			while(maleEnters != maleLeaves) {
				try {
					condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			femaleEnters++;
			femaleKnocks = false;
		}
		finally{
			lock.unlock();
		}
	}

	public void leaveFemale() {
		lock.lock();
		try{
			femaleLeaves++;
			if(femaleEnters == femaleLeaves) {
				condition.signalAll();
			}
		}
		finally{
			lock.unlock();
		}
	}
}
