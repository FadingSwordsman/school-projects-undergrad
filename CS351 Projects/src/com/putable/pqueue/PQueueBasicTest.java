package com.putable.pqueue;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.putable.pqueue.AbstractPQAble;
import com.putable.pqueue.PQAble;
import com.putable.pqueue.PQueue;
import com.putable.pqueue.PQueueBasic;

public class PQueueBasicTest
{
    private PQueue underTest;
    
    
    
    private class MockPQAble extends AbstractPQAble
    {
	private int priority;
	public MockPQAble(int priority)
	{
	    this.priority = priority;
	}
	
	public int getValue()
	{
	    return priority;
	}
	
	@Override
	public int compareTo(PQAble o)
	{
	    MockPQAble obj = (MockPQAble)o;
	    int value = obj.getValue() - getValue();
	    if(value == 0)
		return -1;
	    return value;
	}
	
	@Override
	public String toString()
	{
	    return Integer.toString(priority) + " : " + Integer.toString(getIndex());
	}
	
	@Override
	public boolean equals(Object o)
	{
	    
	    if(!(o instanceof MockPQAble))
		return false;
	    return o.toString().equals(toString());
	}
    }
    
    private List<PQAble> generatePQAbles(int... values)
    {
	List<PQAble> list = new LinkedList<PQAble>();
	for(int next : values)
	    list.add(new MockPQAble(next));
	return list;
    }
    
    private void createPQueue()
    {
	underTest = new PQueueBasic();
    }
    
    @Test
    public void addTest()
    {
	createPQueue();
	List<PQAble> values = generatePQAbles(1,2,3,4,5,6,7,8,9,10);
	for(PQAble value : values)
	    underTest.insert(value);
	
	for(PQAble value : values)
	{
	    assertEquals(value, underTest.remove());
	}
    }
    
    @Test
    public void addTest1()
    {
	createPQueue();
	List<PQAble> values = generatePQAbles(1,1,1,1,1,1,1);
	int index = 1;
	for(PQAble value : values)
    		underTest.insert(value);
	for(PQAble value : values)
	{
	    assertEquals(index, value.getIndex());
	    index++;
	}
    }
    
    @Test
    public void addTest2()
    {
	createPQueue();
	List<PQAble> values = generatePQAbles(1,6,3,5,87,4,23,34,321,3546,6745,54,341,123,345,12,1234,423,4357,21,12,324,
		2354,324,41,52,7,8,67,45,34,0,2,354,46,43,5643,5764,2,4235);
	for(PQAble value: values)
	    underTest.insert(value);
	Collections.sort(values);
	Collections.reverse(values);
	for(PQAble value:values)
	{
	    PQAble next = underTest.remove();
	    assertEquals(value, next);
	    assertEquals(1, value.getIndex());
	}
    }
}
