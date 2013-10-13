package com.putable.pqueue;

public class PQueueBasic implements PQueue
{
    private int size;
    private int capacity;

    private PQAble[] tree;
    
    //Temporary storage for when we expand:
    private PQAble[] oldElements;
    private int nextOldElement;
    
    public PQueueBasic()
    {
	this(15);
    }
    
    public PQueueBasic(int capacity)
    {
	size = 0;
	tree = new PQAble[capacity];
	this.capacity = capacity;
    }
    
    @Override
    public PQAble remove()
    {	
	PQAble value = clearIndex(1);
	heapifyDown();
	size--;
	value.setPQueue(null);
	return value;
    }

    @Override
    public PQAble top()
    {
	if(size > 0)
	    return getPQAbleAt(1);
	return null;
    }

    @Override
    public void insert(PQAble p)
    {
	//Ensure we have some space:
	if(capacity == size + 1)
	    expand();
	else if(oldElements != null)
	    copyNextElementToTree();
	//Add our element:
	tree[size + 1] = p;
	heapifyUp(size+1);
	//Update our trackers:
	size++;
    }

    @Override
    public void delete(PQAble p)
    {
	//This is not an advanced PQueue:
	throw new UnsupportedOperationException(); 
    }

    @Override
    public int size()
    {
	return size;
    }

    /**
     * This Implementation of PQueue is not advanced.
     */
    @Override
    public boolean isAdvanced()
    {
	return false;
    }
    
    /**
     * Heapify down, assuming the first element has already been removed
     */
    private void heapifyDown()
    {
	int forIndex = 1;
	int[] childNodeIndices = getChildrenIndices(forIndex);
	while(childNodeIndices[1] <= size)
	{
	    PQAble left = getPQAbleAt(childNodeIndices[0]);
	    PQAble right = getPQAbleAt(childNodeIndices[1]);
	    if(left.compareTo(right) > 0)
	    {
		set(forIndex, clearIndex(childNodeIndices[0]));
       	    	forIndex = childNodeIndices[0];
	    }
	    else
	    {
		set(forIndex, clearIndex(childNodeIndices[1]));
		forIndex = childNodeIndices[1];
	    }
	    childNodeIndices = getChildrenIndices(forIndex);
	}
	//Make sure the heap is not missing a space:
	if(forIndex != size)
	{
	    set(forIndex, clearIndex(size));
	    heapifyUp(forIndex);
	}
	
    }
    
    /**
     * Heapify up starting from the specified index.
     * @param newChild
     */
    private void heapifyUp(int index)
    {
	PQAble newChild = getPQAbleAt(index);
	int newChildIndex = index;
	int parentIndex = getParentIndex(newChildIndex);
	PQAble parent = getPQAbleAt(parentIndex);
	while(parent != null && newChild.compareTo(parent) > 0)
	{
	    set(newChildIndex, parent);
	    clearOldElement(parentIndex);
	    newChildIndex = parentIndex;
	    parentIndex = getParentIndex(newChildIndex);
	    parent = getPQAbleAt(parentIndex);
	}
	set(newChildIndex, newChild);
	newChild.setPQueue(this);
    }
    
    /**
     * Add or move an element to a spot, and update that element
     * @param index
     * 		
     * @param element
     */
    private void set(int index, PQAble element)
    {
	element.setIndex(index);
	tree[index] = element;
    }
    
    /**
     * Clear the specified element, and return it
     */
    private PQAble clearIndex(int index)
    {
	PQAble element = getPQAbleAt(index);
	tree[index] = null;
	if(oldElements != null && index < oldElements.length)
	    oldElements[index] = null;
	return element;
    }
    
    /**
     * Handle all getting, picking between oldElements and tree
     * @param index
     * @return
     */
    private PQAble getPQAbleAt(int index)
    {
	PQAble toReturn = tree[index];
	if(toReturn == null && oldElements != null && index < oldElements.length)
	    toReturn = oldElements[index];
	return toReturn;
    }
    
    /**
     * Expand the tree space, since we've run out of address space
     */
    private void expand()
    {
	//Make absolutely sure we aren't losing anything:
	while(copyNextElementToTree());
	oldElements = tree;
	tree = new PQAble[oldElements.length << 1];
	capacity = tree.length;
	nextOldElement = 1;
    }
    
    /**
     * Copy an element from the oldElements to the tree sometimes. Over N iterations, the entire tree is copied.
     * 	Naively moves forwards in the list and copies that element down. In the event that that element was affected by Heapify up or down,
     *  no element will be present, since it will have been placed in the tree already.
     * @return
     * 		True if the tree is not complete copying, and false if the old tree has been cleared
     */
    private boolean copyNextElementToTree()
    {
	if(oldElements == null)
	    return false;
	if(nextOldElement < oldElements.length && oldElements[nextOldElement] != null)
	{
	    set(nextOldElement, oldElements[nextOldElement]);
	    oldElements[nextOldElement] = null;
	}
	nextOldElement++;
	if(nextOldElement == oldElements.length)
	    oldElements = null;
	return true;
    }
    
    private void clearOldElement(int index)
    {
	if(oldElements != null && index < oldElements.length)
	    oldElements[index] = null;
    }
    
    private int getParentIndex(int index)
    {
	return index/2;
    }
    
    private int[] getChildrenIndices(int index)
    {
	int firstChild = index << 1;
	return new int[]{firstChild, firstChild + 1};
    }
    
    @Override
    public String toString()
    {
	StringBuilder sb = new StringBuilder();
	for(int x = 1; x <= size; x++)
	    sb.append(getPQAbleAt(x)+" ");
	return sb.toString();
    }
}
