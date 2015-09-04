package q2;

public interface iStack{
	
	public void push(Integer x);
	public Integer pop();
	
	public long getPushTime();
	public long getPopTime();

	public int getPushs();
	public int getPops();
}