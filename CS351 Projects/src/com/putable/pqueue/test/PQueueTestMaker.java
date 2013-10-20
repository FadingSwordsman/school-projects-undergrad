package com.putable.pqueue.test;

import com.putable.pqueue.PQueue;
import com.putable.pqueue.PQueueAdvanced;

/**
 * This class just defines PQueueTestMaker.make, which is the method that is
 * called by the PQueueTest*Maker classes when they need an instance of a PQueue
 * to test.
 * 
 * <p>
 * You <i>are</i> allowed to modify this file, to change the have make() method
 * so that it returns instances of your test class.
 * 
 * @author ackley & you
 * @version 0.9
 * 
 */
public class PQueueTestMaker {
    /**
     * Generate a PQueue instance to test. Modify this method to return
     * instances of whatever PQueue-implementing class you wish to test.
     * 
     * @return a newly-created object that implements interface
     *         com.putable.pqueue.PQueue
     */
    public static PQueue make() {
        return new PQueueAdvanced();
    }

}
