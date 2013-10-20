package com.putable.pqueue.test;

import java.util.PriorityQueue;

import com.putable.pqueue.PQAble;
import com.putable.pqueue.PQueue;

/**
 * A broken and too-slow demonstration-only implementation of a PQueue that
 * depends on Java's PriorityQueue (which is one of JDK Collections that we are
 * <i>not</i> allowed to use in our actual implementation).
 * <p>
 * Testing this class using the supplied test harness (in PQueueTestMaker)
 * should pass everything <i>except</i> some tests related to exception
 * throwing, and tests related to pqueue deleting, such as:
 * <ul>
 * <li>testDelete(Refs) in PQueueTestFunction -- it will fail these tests
 * because it doesn't handle deletion correctly when there is more than one
 * PQAble of the same priority -- it deletes the first matching PQAble found,
 * rather than the specific reference supplied in the delete(PQAble) call.
 * 
 * <li>testSpeedDeleteVaried in PQueueTestBigO -- it will fail this test because
 * its pitiful delete operation is a lame-o O(n) rather than the hip O(log n)
 * that we need. (PQueueSample passes testSpeedDeleteSame because it never has
 * to search to find what it thinks is an appropriate element to delete).
 * </ul>
 * 
 * @author ackley
 * @see PQueueTestMaker
 * 
 */
public class PQueueSample implements PQueue {

    private PriorityQueue<PQAble> pq = new PriorityQueue<PQAble>();

    public void insert(PQAble p) {
        pq.add(p);
    }

    public boolean isAdvanced() {
        return true;  // Not really though..
    }
    
    public void delete(PQAble p) {
        pq.remove(p);
    }

    public PQAble remove() {
        return pq.poll();
    }

    public PQAble top() {
        return pq.peek();
    }

    public int size() {
        return pq.size();
    }

    public String toString() {
        return pq.toString();
    }
}
