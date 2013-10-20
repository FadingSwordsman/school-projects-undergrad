package com.putable.pqueue.test;

import junit.framework.TestCase;

import com.putable.pqueue.PQueue;

/**
 * A base class containing methods common to the different (function, speed)
 * PQueue tests.
 * 
 * @author ackley
 * @version 1.0
 * 
 */

public class TestCommon extends TestCase {

    /**
     * Make a PQueue instance to test.
     * 
     * @return A newly constructed to-be-tested object that implements PQueue
     */
    public PQueue makePQueue() {
        return PQueueTestMaker.make();
    }

    public void testMakePQueue() {
        PQueue pq = makePQueue();
        assertNotNull(pq);
    }

    /**
     * A cheap little way to generate various sequences of integers to use as
     * test keys. For a fixed n, the values returned by get are believed to be
     * all distinct when i < n, at least at n==1000000.
     * 
     * @author ackley
     * 
     */
    public enum Sequence {
        UP {
            public int get(int i, int n) {
                return i;
            }
        },
        DOWN {
            public int get(int i, int n) {
                return n - i;
            }
        },
        DIVERGE {
            public int get(int i, int n) {
                return ((i & 1) == 0) ? i : -i;
            }
        },
        CONVERGE {
            public int get(int i, int n) {
                return (((i & 1) == 0) ? UP : DOWN).get(i / 2, n);
            }
        },
        MIX1 {
            public int get(int i, int n) {
                return ((i * 1771 + 57) * n) ^ i;
            }
        },
        MIX2 {
            public int get(int i, int n) {
                return (i >>> 9) | (i << 23);
            }
        },
        MIX3 {
            public int get(int i, int n) {
                return -((i >>> 3) | (i << 29));
            }
        };
        public abstract int get(int sequenceIndex, int outOfN);
    }
}
