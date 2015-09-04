package q7;

public interface iLinkedList{
   public boolean add(Integer x);
   public boolean remove(Integer x);
   public boolean contains(Integer x);
   
   public long getAddTime();
   public long getRemoveTime();
   public long getContainsTime();
   public int getAdds();
   public int getRemoves();
   public int getContains();
}