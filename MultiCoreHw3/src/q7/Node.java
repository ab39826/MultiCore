package q7;

import java.util.concurrent.atomic.AtomicMarkableReference;


public class Node{
	
	Integer item;
	int key;
	AtomicMarkableReference<Node> next;
	
	Node(Integer x){
		item = x;
		key = x.hashCode();
	}
	
	public boolean add(Integer item){
		int key = item.hashCode();
		/*if(this.next == null){
			this.next = new AtomicMarkableReference<Node>(new Node(item),false);
		}*/
		while(true){
			Window window = find(this, key);
			Node pred = window.pred, curr = window.curr;
			if(curr.key == key){
				return false;
			} else {
				Node node = new Node(item);
				node.next = new AtomicMarkableReference<Node>(curr,false);
				if(pred.next.compareAndSet(curr,node,false,false)){
					return true;
				}
			}
		}
	}
	
	public boolean remove(Integer item){
		int key = item.hashCode();
		boolean snip;
		while(true){
			Window window = find(this,key);
			Node pred = window.pred, curr = window.curr;
			if(curr.key != key){
				return false;
			} else{
				Node succ = curr.next.getReference();
				snip = curr.next.compareAndSet(succ,succ,false,true);
				if(!snip){
					continue;
				}
				pred.next.compareAndSet(curr,succ,false,false);
				return true;
			}
		}
	}
	
	public boolean contains(Integer item){
		boolean[] marked = {false};
		int key = item.hashCode();
		Node curr = this;
		while(curr.key < key){
			if(curr.next == null){
				return false;
			}
			curr = curr.next.getReference();
			if(curr.next == null){
				return false;
			}
			Node succ = curr.next.get(marked);
		}
		return (curr.key == key && !marked[0]);
	}

	public Window find(Node head, int key){
		Node pred = null, curr = null, succ = null;
		boolean[] marked = {false};
		boolean snip;
		retry: while(true){
			pred = head;
			curr = pred.next.getReference();
			while(true){
				if(curr.next == null){
					return new Window(pred,curr);
				}
				succ = curr.next.get(marked);
				while(marked[0]){
					snip = pred.next.compareAndSet(curr,succ,false,false);
					if(!snip) continue retry;
					curr = succ;
					succ = curr.next.get(marked);
				}
				if(curr.key >= key)
					return new Window(pred,curr);
				pred = curr;
				curr = succ;
			}
		}
	}
	
}