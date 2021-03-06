package edu.unm.cs.locd011;

public class Tuple<T> {
	private T first = null;
	private T second = null;
	private boolean isFull = false;
	
	/**
	 * Create a tuple with only one value
	 * @param value
	 */
	public Tuple(T value){
		first = value;
	}
	
	/**
	 * Create a tuple with two values
	 * @param firstValue
	 * @param secondValue
	 */
	public Tuple(T firstValue, T secondValue){
		first = firstValue;
		second = secondValue;
		isFull = (firstValue != null) && (secondValue != null);
	}
	
	/**
	 * Set the first tuple value
	 * @param value
	 */
	public T setFirst(T value){
		T temp = first;
		first = value;
		isFull = (value != null) && (second != null);
		return temp;
	}
	 /**
	  * Set the second tuple value
	  * @param value
	  */
	public T setSecond(T value){
		T temp = second;
		second = value;
		isFull = (value != null) && (first != null);
		return temp;
	}
	
	/**
	 * Returns the first stored value
	 * @return
	 */
	public T getFirst(){
		return first;
	}
	
	/**
	 * Returns the second stored value
	 * @return
	 */
	public T getSecond(){
		return second;
	}
	
	/**
	 * Checks to see if both tuple values are set
	 * @return
	 */
	public boolean isFull(){
		return isFull;
	}
	
	/**
	 * Outputs the first and second values as an ordered pair
	 */
	@Override
	public String toString(){
		return "(" + first + ", " + second + ")";
	}
}
