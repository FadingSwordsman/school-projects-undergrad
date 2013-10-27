package com.putable.pqueue;

/**
 * An advanced version of a PQueue that supports O(log(n)) remove operations.
 * @author David
 *
 */
public class PQueueAdvanced implements PQueue
{
    private int size;
    private PQAble[] tree;
    
    private int nextCopy;
    private PQAble[] oldElements;

    /**
     * Create a default instance of this class
     */
    public PQueueAdvanced()
    {
	this(10);
    }
    
    public PQueueAdvanced(int capacity)
    {
	this.size = 0;
	tree = new PQAble[capacity];
    }
    
    @Override
    public PQAble remove()
    {
	if(size > 0)
	{
	    PQAble value = getPQAbleAt(1);
	    value.setPQueue(null);
	    PQAble newHead = getPQAbleAt(size);
	    set(size, null);
	    size--;
	    if(size > 0)
		heapifyDown(1, newHead);
	    return value;
	}
	return null;
    }

    @Override
    public PQAble top()
    {
	return getPQAbleAt(1);
    }

    @Override
    public void insert(PQAble p)
    {
	if(p == null)
	    throw new NullPointerException("p");
	if(p.getPQueue() != null)
	    throw new IllegalStateException("p is already on another PQueue!");
	while(size+1 >= tree.length)
	    expand();
	size++;
	set(size, p);
	p.setPQueue(this);
	heapifyUp(size);
    }

    @Override
    public void delete(PQAble p)
    {
	if(p == null)
	    throw new NullPointerException();
	if(p.getPQueue() != this)
	    throw new IllegalStateException("p not on this PQueue!");
	int index = p.getIndex();
	set(index, null);
	p.setPQueue(null);
	PQAble moved = getPQAbleAt(size);
	set(size, null);
	size--;
	if(index <= size)
	{
	    heapifyDown(index, moved);
	    heapifyUp(index);
	}
    }

    @Override
    public int size()
    {
	return size;
    }

    @Override
    public boolean isAdvanced()
    {
	return true;
    }
    
    private void heapifyUp(int fromIndex)
    {
	int currentIndex = fromIndex;
	PQAble moving = getPQAbleAt(currentIndex);
	int parentIndex = getParentIndex(currentIndex);
	PQAble parent = getPQAbleAt(parentIndex);
	while(parentIndex > 0 && isHigherPriority(moving, parent))
	{
	    set(currentIndex, parent);
	    currentIndex = parentIndex;
	    parentIndex = getParentIndex(currentIndex);
	    parent = getPQAbleAt(parentIndex);
	}
	set(currentIndex, moving);
    }
    
    /**
     * Heapify up, as though inserting insert at the given index
     * @param fromIndex
     * @param insert
     */
    private void heapifyDown(int fromIndex, PQAble insert)
    {
	int currentIndex = fromIndex;
	int[] currentChildIndices = getChildIndices(currentIndex);
	PQAble left = getPQAbleAt(currentChildIndices[0]);
	PQAble right = getPQAbleAt(currentChildIndices[1]);
	while(currentChildIndices[0] <= size && !(isHighestPriority(insert, left, right)))
	{
	    if(isHigherPriority(left, right))
	    {
		set(currentIndex, left);
		currentIndex = currentChildIndices[0];
	    }
	    else
	    {
		set(currentIndex, right);
		currentIndex = currentChildIndices[1];
	    }
	    currentChildIndices = getChildIndices(currentIndex);
	    left = getPQAbleAt(currentChildIndices[0]);
	    right = getPQAbleAt(currentChildIndices[1]);
	}
	set(currentIndex, insert);
    }
    
    private boolean isHigherPriority(PQAble first, PQAble second)
    {
	if(first == null)
	    return false;
	else if(second == null)
	    return true;
	
	return first.compareTo(second) < 0;
    }
    
    private boolean isHighestPriority(PQAble first, PQAble...seconds)
    {
	boolean result = true;
	for(PQAble next : seconds)
	    result = result && isHigherPriority(first, next);
	return result;
    }
    
    private int getParentIndex(int ofIndex)
    {
	return ofIndex >> 1;
    }
    
    private int[] getChildIndices(int ofIndex)
    {
	int firstChild = ofIndex << 1;
	int secondChild = firstChild + 1;
	return new int[]{firstChild, secondChild};
    }
    
    private void set(int index, PQAble item)
    {
	tree[index] = item;
	if(oldElements != null && index < oldElements.length)
	    oldElements[index] = null;
	if(item != null)
	    item.setIndex(index);
    }
    
    private PQAble getPQAbleAt(int index)
    {
	if(index <= size && index > 0)
	{
	    PQAble item = tree[index];
	    if(oldElements != null && item == null && index < oldElements.length)
		item = oldElements[index];
	    return item;
	}
	return null;
    }
    
    private void expand()
    {
	//Clear the list:
	while(copyOldElement());
	//Update the new list:
	oldElements = tree;
	//Return a new list:
	tree = new PQAble[tree.length << 1];
	nextCopy = 1;
    }
    
    private boolean copyOldElement()
    {
	boolean copy = oldElements != null && nextCopy < oldElements.length;
	if(copy)
	{
	    if(tree[nextCopy] == null)
		tree[nextCopy] = oldElements[nextCopy];
	    nextCopy++;
	}
	return copy;
    }
    
    @Override
    public String toString()
    {
	StringBuilder sb = new StringBuilder();
	for(int x = 1; x <= size; x++)
	    sb.append('[').append(getPQAbleAt(x)).append(']');
	return sb.toString();
    }
}
