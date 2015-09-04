package q2;

public interface iQueue{
	
	public void enqueue(Integer x);
	public Integer dequeue();
	
	public long getEnqTime();
	public long getDeqTime();

	public int getEnqs();
	public int getDeqs();
}