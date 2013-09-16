// Helper class for SizeKSubset.

package edu.unm.cs.cs361.f12.locd011.subsets;
import java.util.*;   // (Collections framework)

/**
 * A lazy iterator for the SizeKSubset object.
 * 
 * @author David Lochridge
 *
 * @param <E>
 */
public class SizeKSubsetIterator<E> implements Iterator<Set<E>> { 

	 private SizeKSubset<E> theSet;

	 private int generated = 0;
	 
	 private LinkedList<E> currentSet;
	 private Deque<Set<E>> frames;
	 
	 /**
	  * Create a new instance of our subset iterator
	  * @param theSet The subset collection over which we are iterating 
	  */
	 public SizeKSubsetIterator ( SizeKSubset<E> theSet ) {
		this.theSet = theSet; 
		currentSet = new LinkedList<E>();
		frames = new LinkedList<Set<E>>();
		frames.push(theSet.backingSet);
	 }
	 
	 /**
	  * Check to see if there are any subsets which have not been returned
	  */
	 public boolean hasNext() {
		// Returns true if the iteration has more elements.
		 return generated < theSet.size();
	 }
	 
	 /**
	  * Return the next ungenerated subset
	  */
	 public Set<E> next( ) {
		// Returns the next element in the iteration.
		// This will always be a size-k subset of the Set<E>
		// that is backing theSet.
		 generated++;
		 Set<E> next = findNextElement(theSet.k - currentSet.size(), frames.pop());
		 currentSet.pop();
		 return next;
	 }
	 
	 /**
	  * Find the next element we haven't already
	  * Also, simulate a stack as though we were recursively generating the subsets
	  * @param k The size of the subset to generate, from the remaining elements
	  * @param remainingElements Elements which have not been placed in the current subset
	  * @return The next subset
	  */
	 private Set<E> findNextElement(int k, Set<E> remainingElements){
		 int nextK = k;
		 if(k == 0){
			 return new HashSet<E>(currentSet);
		 } else if(k > remainingElements.size()){
			 if(currentSet.size() > 0){
				 nextK++;
				 currentSet.pop();
			 }
			 return findNextElement(nextK, frames.pop());
		 }
		 E nextElement = remainingElements.iterator().next();
		 remainingElements.remove(nextElement);
		 Set<E> remaining = new HashSet<E>();
		 frames.push(remainingElements);
		 remaining.addAll(remainingElements);
		 currentSet.push(nextElement);
		 //Tail recursion ftw
		 nextK--;
		 return findNextElement(nextK, remaining);
	 }

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
		
	}
}