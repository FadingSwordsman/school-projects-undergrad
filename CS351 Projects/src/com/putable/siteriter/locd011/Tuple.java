package com.putable.siteriter.locd011;

/**
 * 
 * Convenience class for grouping different items together
 * @author David
 * @param <T>
 * @param <U>
 */
public class Tuple<T, U>
{
    T first;
    U second;

    public Tuple(T first, U second)
    {
	this.first = first;
	this.second = second;
    }

    public T getFirst()
    {
	return first;
    }

    public U getSecond()
    {
	return second;
    }

    @Override
    public String toString()
    {
	return first.toString() + " : " + second.toString();
    }

   @Override
   public boolean equals(Object o)
   {
	if(!o instanceof Tuple.class)
		return false;
	Tuple<?,?> obj = (Tuple<?,?>) o;
	if(!obj.getFirst() instanceof first.class)
		return false;
	if(!obj.getSecond() instanceof second.class)
		return false;
	T first = (T) obj.getFirst(); 
	U second = (U) obj.getSecond();
	return this.first.equals(first) && this.second.equals(second);
   }
}
