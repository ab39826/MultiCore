package q5;

public class Worker implements Runnable{

	boolean male = true;
	BathroomLockProtocol bathroom;
	

	public Worker(boolean male, BathroomLockProtocol bathroom) {
		this.male = male;
		this.bathroom = bathroom;
	}
	
	@Override
	public void run() {
		if(male) {
			bathroom.enterMale();
			int randomNum = 1 + (int)(Math.random()*5);
			int i = 0;
			while(i<(randomNum*100000)){
			i++;	
			}
			bathroom.leaveMale();
		}
		else{
			bathroom.enterFemale();
			int randomNum = 1 + (int)(Math.random()*5);
			int i = 0;
			while(i<(randomNum*1000)){
			i++;	
			}
			bathroom.leaveFemale();
		}
		
	}
	
}
