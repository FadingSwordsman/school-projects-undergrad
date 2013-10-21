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
	size = 0;
	tree = new PQAble[10];
    }
    
    @Override
    public PQAble remove()
    {
	if(size > 0)
	{
	    PQAble value = getPQAbleAt(1);
	    value.setPQueue(null);
	    heapifyDown(1);
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
	PQAble newChild = getPQAbleAt(fromIndex);
	int newChildIndex = fromIndex;
	int parentIndex = getParentIndex(newChildIndex);
	PQAble parent = getPQAbleAt(parentIndex);
	while (parent != null && parent.compareTo(newChild) < 0)
	{
	    set(newChildIndex, parent);
	    newChildIndex = parentIndex;
	    parentIndex = getParentIndex(newChildIndex);
	    parent = getPQAbleAt(parentIndex);
	}
	set(newChildIndex, newChild);
	newChild.setPQueue(this);
    }
    
    /**
     * Heapify up, clobbering the element in fromIndex
     * @param fromIndex
     */
    private void heapifyDown(int fromIndex)
    {
	int currentIndex = fromIndex;
	set(currentIndex, getPQAbleAt(size));
	set(size, null);
	int[] currentChildIndices = getChildIndices(currentIndex);
	while (currentChildIndices[0] <= size)
	{
	    PQAble parent = getPQAbleAt(currentIndex);
	    currentChildIndices = getChildIndices(currentIndex);
	    PQAble left = getPQAbleAt(currentChildIndices[0]);
	    PQAble right = getPQAbleAt(currentChildIndices[1]);
	    PQAble nextIndex = getHighestPriority(left, right);
	    nextIndex = getHighestPriority(nextIndex, parent);
	    if(nextIndex.getIndex() != parent.getIndex())
	    {
		
	    }
	}
	/**int currentIndex = fromIndex;
	PQAble nextItem = getPQAbleAt(fromIndex);
	int[] currentChildIndices = getChildIndices(currentIndex);
	while(currentChildIndices[0] <= size)
	{
	    PQAble left = getPQAbleAt(currentChildIndices[0]);
	    PQAble right = getPQAbleAt(currentChildIndices[1]);
	    PQAble higherPriority = getHighestPriority(left, right);
	    int newIndex = higherPriority.getIndex();
	    currentChildIndices = getChildIndices(newIndex);
	    set(currentIndex, higherPriority);
	    currentIndex = newIndex;
	}
	PQAble moveElement = getPQAbleAt(size);
	set(size, null);
	size--;
	if(currentIndex <= size)
	{
	    heapifyDown(currentIndex, moveElement);
	    heapifyUp(currentIndex);
	}*/
    }
    
    /**
     * Heapify up, as though inserting insert at the given index
     * @param fromIndex
     * @param insert
     */
    private void heapifyDown(int fromIndex, PQAble insert)
    {
	
	/*
	int currentIndex = fromIndex;
	int[] currentChildIndices = getChildIndices(currentIndex);
	boolean heapified = false;
	while(!heapified && currentChildIndices[0] < size)
	{
	    PQAble left = getPQAbleAt(currentChildIndices[0]);
	    PQAble right = getPQAbleAt(currentChildIndices[1]);
	    PQAble higherPriority = getHighestPriority(left, right);
	    higherPriority = getHighestPriority(higherPriority, insert);
	    int newIndex = higherPriority.getIndex();
	    set(currentIndex, higherPriority);
	    set(newIndex, null);
	    heapified = higherPriority == insert;
	    currentIndex = newIndex;
	    currentChildIndices = getChildIndices(currentIndex);
	}
	if(currentChildIndices[0] > size)
	    set(currentIndex, insert);*/
    }
    
    private PQAble getHighestPriority(PQAble first, PQAble second)
    {
	if(first == null)
	    return second;
	else if(second == null)
	    return first;
	
	return first.compareTo(second) < 0 ? first : second;
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
}
