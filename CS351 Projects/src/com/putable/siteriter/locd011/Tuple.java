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
}