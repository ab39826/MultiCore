package q6;

import java.util.concurrent.atomic.AtomicReference;
public class MCSLock implements MyLock {
	AtomicReference<QNode> tail;
	ThreadLocal<QNode> myNode;
	
	
	
	public MCSLock() {
		
		tail = new AtomicReference<QNode>(null);
		myNode = new ThreadLocal<QNode>(){
			protected QNode initialValue() {
				return new QNode();
			}
		};
		
		
	}
	
	
	@Override
	public void lock() {
		// TODO Auto-generated method stub
		QNode qnode = myNode.get();
		QNode pred = tail.getAndSet(qnode);
		
		if(pred != null){
			qnode.locked = true;
			pred.next = qnode;
			//wait until predeccessor gives up lock
			
			while(qnode.locked){}
		}	
	}

	@Override
	public void unlock() {
		// TODO Auto-generated method stub
		QNode qnode = myNode.get();
		if(qnode.next == null) {
			if(tail.compareAndSet(qnode, null))
				return;
			//wait until successor fills in next field
			while(qnode.next == null){}
		}
		qnode.next.locked = false;
		qnode.next = null;
	}
	
	private static class QNode {
		private volatile boolean locked;
		volatile QNode next = null;
	}
	
}