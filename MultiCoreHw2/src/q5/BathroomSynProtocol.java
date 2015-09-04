package q5;

//TODO
//Use synchronized, wait(), notify(), and notifyAll() to implement the bathroom
//protocol
public class BathroomSynProtocol implements Protocol {

	
	public volatile Integer maleTotal = 0;
	public volatile Integer femaleTotal = 0;

	public volatile Boolean femaleKnocks = false;
	public volatile Boolean maleKnocks = false;


	/*
	 * Notes: This is basically the same as what we have to do for
	 * bathroomlockprotocol. Seems notifyAll() is not workiing properly???
	 * 
	 * Ask professor why we can't use primitive ints here?
	 */
	
	

	public void enterMale() {
		try {
			synchronized (maleTotal) {

				while (femaleKnocks) {
					maleTotal.wait(5000);
				}

				while ((femaleTotal.intValue() > 0)) {
					maleKnocks = true;
					maleTotal.wait(5000);
				}
				maleTotal++;
				maleKnocks = false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void leaveMale() {
		synchronized (maleTotal) {
			maleTotal--;
			if (maleTotal.intValue() == 0) {
				synchronized (maleTotal) {
					maleKnocks = false;

					synchronized (femaleTotal) {
						femaleTotal.notifyAll();
					}

				}

			}
		}
	}

	public void enterFemale() {
		try {
			synchronized (femaleTotal) {

				while (maleKnocks) {
					femaleTotal.wait(5000);
				}

				while (maleTotal.intValue() > 0) {
					femaleKnocks = true;
					femaleTotal.wait(5000);
				}
				femaleTotal++;
				femaleKnocks = false;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void leaveFemale() {
		synchronized (femaleTotal) {
			femaleTotal--;
			if (femaleTotal.intValue() == 0) {
				synchronized (femaleTotal) {
					femaleKnocks = false;

					synchronized (maleTotal) {
						maleTotal.notifyAll();
					}
				}

			}
		}
	}

}
