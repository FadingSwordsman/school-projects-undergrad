package com.putable.pqueue.test;

import com.putable.pqueue.PQueue;
import com.putable.timing.AbstractBigOTest;
import com.putable.timing.BigO;

/**
 * A class that (attempts to) test the big-O properties of a PQueue-implementing
 * class. The class-to-be-tested must be specified in advance by modifying
 * com.putable.pqueue.test.PQueueTestMaker.make() appropriately.
 * <p>
 * If you don't modify PQueueTestMaker.make(), you will by default be testing
 * the class {@link PQueueSample}, a PQueue implementation that delegates all
 * operations to a java.util.PriorityQueue, which fails to do some necessary
 * argument checking, and which handles deletion incorrectly for our purposes,
 * and so consequently will fail various tests.
 * <p>
 * <i>Note that running the tests in this class will take <b>a lot of time</b>
 * -- on the order of <b>minutes</b> rather than seconds or milliseconds of
 * wall-clock time -- no matter how efficient the PQueue-under-test actually
 * is.</i>
 * <p>
 * Testing the asymptotic time efficiency of code is difficult for at least two
 * reasons:
 * <ol>
 * <li>Getting accurate runtime measurements, and
 * <li>Having an appropriate and sufficient range of sizes of problem size
 * <i>n</i> to observe the asymptotic behavior of the code being tested.
 * </ol>
 * <p>
 * Java makes the first problem particularly acute, since it provides no
 * mechanism for obtaining specifically runtime rather than overall time
 * elapsed. So timing tests run on identical code will produce varied results
 * depending on how busy is the machine used to run the tests. Varying non-test
 * workloads on the testing machine can erase any possibility of accurately
 * measuring the time efficiency of some piece of code-- so it's important to
 * <i>run these tests on as 'quiet' a machine as is available</i>.
 * <p>
 * The second problem is a challenge as well, if the code potentially being
 * tested may have a wide range of actual time efficiencies. Two different
 * implementations of the same operation might both be O(n), say, but one
 * implementation might take 10 seconds when <i>n</i> is a million, while the
 * other might take 10 minutes or 10 hours at the same problem size.
 * <p>
 * This testing code does not attempt to be completely general regarding a
 * solution to this second problem. This testing code is simply preloaded with
 * certain specific amounts of time to take per test and a certain range of
 * problem sizes depending what is being tested. The testing code is designed
 * simply to keep repeating a given test at a given size until the allotted time
 * has been all used up. These (hopefully many) test iterations are then
 * averaged to improve the estimate of the actual time needed for a given
 * operation at a given problem size.
 * <p>
 * If code under test is running so slowly that the time-averaging loop is able
 * to run less than 3 times before the allotted time is consumed, a '<b>Low
 * iteration count</b>' warning will be printed to System.err by the timing code
 * (in com.putable.timing.BigOTest). This can be caused by (any combination of)
 * at least three different factors:
 * <ol>
 * <li>The machine the test is being run on is very slow compared to the speed
 * of the machine used when the test parameters were developed -- or the testing
 * machine is very busy, lowering its effective speed as seen by the testing
 * code. If this is the problem then <i>no conclusion can be drawn</i> about
 * whether or not the code under test has the correct big-O.
 * 
 * <li>The constant factor of the code under test might be so large, compared to
 * the implementations used when the test parameters were developed, that the
 * timing periods and problem sizes are simply inappropriate for that
 * implementation. In this situation, it is certainly <i>possible</i> that the
 * code being tested is perfectly fine in asymptotic efficiency, but the test as
 * configured will be unable to detect it. Contact the author of this testing
 * code if you suspect you have an instance of correct big-O code failing for
 * this reason.
 * 
 * <li>Or finally, the code under test may really <i>not</i> be achieving the
 * expected big-O time efficiency.
 * </ol>
 * 
 * @author ackley
 * @see PQueueTestFunction
 * 
 */
public class PQueueTestBigO extends TestCommon {

    private class BigOTestSize extends AbstractBigOTest {
 
        private PQueue pq;

        public BigOTestSize() {
            super(BigO.CONSTANT, 1 << 10, 6, 4);
        }

        public void reset(int repeats) {
            dontOptimizeMeBro = repeats;
            pq = makePQueue();
            for (int i = 0; i < dontOptimizeMeBro; ++i) {
                pq.insert(new TestElement(i));
            }
        }

        public final void doIt(int repeat) {
            dontOptimizeMeBro = repeat;
            for (int i = 0; i < 10000; ++i)
                // Puff up size() by looping -- it's too fast
                if (pq.size() < dontOptimizeMeBro)
                    throw new IllegalStateException();
        }
    }

    /** Test that size() appears to be O(1). */
    public void testSpeedSize() {
        AbstractBigOTest t = new BigOTestSize();
        t.assertSpeed();
    }

    private class BigOTestInsert extends AbstractBigOTest {
        private PQueue pq;

        private boolean samePrio;

        public BigOTestInsert(boolean samePrio) {
            super(BigO.LOG, 1 << 11, 6, 5);
            this.samePrio = samePrio;
        }

        public void reset(int repeats) {
            pq = makePQueue();
        }

        public final void doIt(int repeat) {
            if (samePrio) {
                pq.insert(new TestElement(1));
                pq.insert(new TestElement(2));
            } else {
                pq.insert(new TestElement(103 * repeat));
                pq.insert(new TestElement(10000 - 66 * repeat));
            }
        }
    }

    /** Test speed of insertion when priority values are mostly unique. */
    public void testSpeedInsertVaried() {
        new BigOTestInsert(false).assertSpeed();
    }

    /** Test speed of insertion when priority values are mostly repeated. */
    public void testSpeedInsertSame() {
        new BigOTestInsert(true).assertSpeed();
    }

    private class BigOTestRemove extends AbstractBigOTest {
        private boolean samePrio;

        private PQueue pq;

        private TestElement[] cases;

        public BigOTestRemove(boolean samePrio) {
            super(BigO.LOG, 1 << 11, 6, 5);
            this.samePrio = samePrio;
        }

        public void reset(int repeats) {
            pq = makePQueue();
            cases = new TestElement[repeats];
            dontOptimizeMeBro = repeats;
            for (int i = 0; i < dontOptimizeMeBro; ++i) {
                int num = (samePrio ? 99 : ((i & 1) == 0 ? i / 2 : repeats - i
                        / 2));
                cases[repeats - i - 1] = new TestElement(num);
                pq.insert(cases[repeats - i - 1]);
            }
        }

        public final void doIt(int repeat) {
            pq.remove();
        }
    }

    /** Test speed of remove() when priority values are mostly unique. */
    public void testSpeedRemoveVaried() {
        new BigOTestRemove(false).assertSpeed();
    }

    /** Test speed of remove() when priority values are mostly repeated. */
    public void testSpeedRemoveSame() {
        new BigOTestRemove(true).assertSpeed();
    }

    private class BigOTestDelete extends AbstractBigOTest {
        private boolean samePrio;

        private PQueue pq;

        private TestElement[] cases;

        public BigOTestDelete(boolean samePrio) {
            super(BigO.LOG, 1 << 11, 6, 5);
            this.samePrio = samePrio;
        }

        public void reset(int repeats) {
            pq = makePQueue();
            cases = new TestElement[repeats];
            dontOptimizeMeBro = repeats;
            for (int i = 0; i < dontOptimizeMeBro; ++i) {
                int num = (samePrio ? 99 : ((i & 1) == 0 ? i / 2 : repeats - i
                        / 2));
                cases[repeats - i - 1] = new TestElement(num);
                pq.insert(cases[repeats - i - 1]);
            }
        }

        public final void doIt(int repeat) {
            pq.delete(cases[repeat]);
        }
    }

    /** Test speed of delete() when priority values are mostly unique. */
    public void testSpeedDeleteVaried() {
        new BigOTestDelete(false).assertSpeed();
    }

    /** Test speed of delete() when priority values are mostly repeated. */
    public void testSpeedDeleteSame() {
        new BigOTestDelete(true).assertSpeed();
    }

    private class BigOTestRemoveAfterDelete extends AbstractBigOTest {
        public BigOTestRemoveAfterDelete() {
            super(BigO.CONSTANT, 64, 5, 11);
        }

        private PQueue[] pq;

        public void reset(int repeats) {

            // Here, before the clock starts, we're going to generate n pqueues,
            // and then for each one, we'll insert n elements and then delete
            // all but one. So we'll be prepped with n size 1 pq's each of
            // which has had a bunch of inserts and deletes. Removing the
            // one remaining element from the pq should then be a constant
            // time operation per pq.

            pq = new PQueue[repeats];
            dontOptimizeMeBro = repeats;
            for (int i = 0; i < dontOptimizeMeBro; ++i) {
                pq[i] = makePQueue();
                TestElement[] cases = new TestElement[repeats];
                for (int j = 0; j < dontOptimizeMeBro; ++j) {
                    cases[j] = new TestElement(j);
                    pq[i].insert(cases[j]);
                }
                for (int j = 0; j < repeats; ++j) {
                    if (j == i)
                        continue; // Leave one element in
                    pq[i].delete(cases[j]);
                }
            }
        }

        public final void doIt(int repeat) {
            assertNotNull(pq[repeat].remove());
            assertNull(pq[repeat].remove());
        }
    }

    /**
     * Test speed of remove() when there's only one element left in the pqueue,
     * so the result should be O(1). However, we'll have done n inserts and n-1
     * deletes ahead of time, so if an implementation bogusly attempts to speed
     * up delete() by leaving 'holes' that remove() then has to skip over, that
     * will hopefully be detected here.
     */

    public void testSpeedRemoveAfterDelete() {
        AbstractBigOTest t = new BigOTestRemoveAfterDelete();
        t.assertSpeed();
    }

    private class BigOTestToString extends AbstractBigOTest {
        private PQueue pq;

        public BigOTestToString() {
            super(BigO.LINEAR, 1 << 3, 6, 4);
        }

        public void reset(int repeats) {
            pq = makePQueue();
            dontOptimizeMeBro = repeats;
            for (int i = 0; i < dontOptimizeMeBro; ++i) {
                pq.insert(new TestElement(i));
            }
        }

        public final void doIt(int repeat) {
            pq.toString();
        }
    }

    /** Test that toString() seems to be O(n) */
    public void testSpeedToString() {
        AbstractBigOTest t = new BigOTestToString();
        t.assertSpeed();
    }

}
