package com.fadingswordsman.data;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Vector<T> implements List<T>
{
	AbstractList<T[]> sublists = new ArrayList<T[]>();
	int subListSize = 16;

	int subLists = 0;
	int size = 0;
	
	@Override
	public int size()
	{
		return size;
	}

	@Override
	public T get(int index)
	{
		int array = index/subListSize;
		int subIndex = index % subListSize; 
		return sublists.get(array)[subIndex];
	}

	@Override
	public boolean add(T e)
	{
		add(size, e);
		size++;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void add(int index, T element)
	{
		if(index > size)
			throw new IndexOutOfBoundsException("Sparse elements are not allowed");
		if(index != size)
			shift(index, 1);
		int arrayIndex = index / subListSize;
		int subIndex = index % subListSize;
		if(index == size)
		{
			sublists.add((T[])new Object[subListSize]);
			subLists++;
		}
		sublists.get(arrayIndex)[subIndex] = element;
	}

	@Override
	public boolean addAll(Collection<? extends T> c)
	{
		boolean success = true;
		for(T next : c)
			success = success && add(next);
		return success;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c)
	{
		shift(index, c.size());
		int currIndex = index;
		boolean success = true;
		for(T next : c)
		{
			set(currIndex, next);
			currIndex++;
		}
		return success;
	}

	@Override
	public void clear()
	{
		sublists.clear();
		size = 0;
		subLists = 0;
	}
	
	@Override
	public boolean contains(Object o)
	{
		if(size == 0)
			return false;
		if(o == null || !get(0).getClass().isInstance(o))
			return false;
		@SuppressWarnings("unchecked")
		T obj = (T) o;
		for(T next : this)
			if(next.equals(obj))
				return true;
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c)
	{
		for(Object n : c)
			if(!contains(n))
				return false;
		return true;
	}

	@Override
	public int indexOf(Object o)
	{
		int index = 0;
		for(T next : this)
		{
			if(next.equals(o))
				return index;
			index++;
		}
		return -1;
	}

	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}

	@Override
	public Iterator<T> iterator()
	{
		return new Iterator<T>()
		{
			int i = 0;
			
			@Override
			public boolean hasNext()
			{
				return i < size;
			}

			@Override
			public T next()
			{
				T next = get(i);
				i++;
				return next;
			}

			@Override
			public void remove()
			{
				Vector.this.remove(i-1);
			}
			
		};
		
	}

	@Override
	public int lastIndexOf(Object o)
	{
		for(int i = size - 1; i > -1; i--)
			if(get(i).equals(o))
				return i;
		return -1;
	}

	@Override
	public ListIterator<T> listIterator()
	{
		return listIterator(0);
	}

	@Override
	public ListIterator<T> listIterator(final int index)
	{
		return new ListIterator<T>()
		{
			int i = index;
			
			int lastElementReturned = -1;
			
			@Override
			public void add(T arg0)
			{
				Vector.this.add(i, arg0);
				i++;
			}

			@Override
			public boolean hasNext()
			{
				return i < size;
			}

			@Override
			public boolean hasPrevious()
			{
				return i > 0;
			}

			@Override
			public T next()
			{
				T next = get(i);
				lastElementReturned = i;
				i++;
				return next;
			}

			@Override
			public int nextIndex()
			{
				return i;
			}

			@Override
			public T previous()
			{
				i--;
				lastElementReturned = i;
				return get(i);
			}

			@Override
			public int previousIndex()
			{
				return i-1;
			}

			@Override
			public void remove()
			{
				Vector.this.remove(lastElementReturned);
			}

			@Override
			public void set(T arg0)
			{
				Vector.this.set(lastElementReturned, arg0);
			}
		};
	}

	@Override
	public boolean remove(Object o)
	{
		for(int i = 0; i < size; i++)
			if(get(i).equals(o))
			{
				shift(i+1, -1);
				return true;
			}
		return false;
	}

	@Override
	public T remove(int index)
	{
		T element = get(index);
		shift(index + 1, -1);
		return element;
	}

	@Override
	public boolean removeAll(Collection<?> c)
	{
		int offset = 0;
		boolean changed = false;
		for(int i = 0; i < size; i++)
			if(c.contains(get(i)))
			{
				offset++;
				set(i, null);	
				changed = true;
			}
			else if(offset > 0)
				set(i-offset, get(i));
		size -= offset;
		cleanSublists();
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> c)
	{
		for(T next : this)
			if(!c.contains(next))
				remove(next);
		return true;
	}

	@Override
	public T set(int index, T element)
	{
		T displaced = get(index);
		int arrayIndex = index / subListSize;
		int sublistIndex = index % subListSize;
		sublists.get(arrayIndex)[sublistIndex] = element;
		return displaced;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex)
	{
		List<T> subList = new Vector<T>();
		for(int i = fromIndex; i < toIndex; i++)
			subList.add(get(i));
		return subList;
	}

	@Override
	public Object[] toArray()
	{
		Object[] toReturn = new Object[size];
		for(int i = 0; i < size; i++)
			toReturn[i] = get(i);
		return toReturn;
	}

	@Override
	public <U> U[] toArray(U[] a)
	{
		//TODO: Return an array of U's if all of the elements can be cast to U 
		return null;
	}
	
	private void shift(int index, int offset)
	{
		int offsetRemaining = offset;
		int offsetDirection = (int)Math.signum(offset);
		int arrOffset = (subListSize - index) % subListSize;
		
		//TODO: Write a shift to move elements in an efficient way
		while(Math.abs(offsetRemaining) >= subListSize)
		{
			int distance = subListSize * offsetDirection;
			offsetRemaining -= distance;
		}
	}
	
	private void cleanSublists()
	{
		int lastArray = size / subListSize;
		int counter = 0;
		for(int i = lastArray; i < subLists; i++)
		{
			sublists.remove(lastArray + 1);
			counter++;
		}
		subLists -= counter;
	}
}