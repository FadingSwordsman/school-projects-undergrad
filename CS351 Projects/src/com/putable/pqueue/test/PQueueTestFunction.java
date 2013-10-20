package com.putable.pqueue.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.TreeSet;

import com.putable.pqueue.PQAble;
import com.putable.pqueue.PQueue;

/**
 * A (subset of a larger) class that runs functional tests on a
 * PQueue-implementing class. The class-to-be-tested must be specified in
 * advance by modifying com.putable.pqueue.test.PQueueTestMaker.make()
 * appropriately.
 * <p>
 * If you don't modify PQueueTestMaker.make(), you will by default be testing
 * the class {@link PQueueSample}, a PQueue implementation that delegates all
 * operations to a java.util.PriorityQueue, which fails to do some necessary
 * argument checking, and which handles deletion incorrectly for our purposes,
 * and so consequently will fail various tests.
 * <p>
 * Note that this class attempts to test <i>only</i> correct input-output
 * behaviors for the PQueue-implementing class -- it does <i>not</i> attempt to
 * check if the required big-O time efficiencies are satisfied.
 * 
 * @author ackley
 * @see PQueueTestBigO
 * 
 */
public class PQueueTestFunction extends TestCommon {

    private PQueue pq0, pq1;

    /**
     * Recursively generate all permutations of a list of integers. In the
     * spirit of the 'let's take off and nuke them from orbit' testing
     * principle..
     * 
     * @param l
     *            the list of integers to find all permutations of.
     * @return a list of (array)lists of integers representing all permutations
     *         of l.
     */
    private static List<List<Integer>> allPermutations(List<Integer> l) {
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        if (l.size() == 0) {
            ret.add(new ArrayList<Integer>());
            return ret;
        }
        for (int i = 0; i < l.size(); ++i) {

            ArrayList<Integer> allButI = new ArrayList<Integer>();
            for (int j = 0; j < l.size(); ++j)
                if (j != i)
                    allButI.add(l.get(j));

            for (List<Integer> li : allPermutations(allButI)) {
                li.add(l.get(i));
                ret.add(li);
            }
        }
        return ret;
    }

    private List<Integer> permInputs = new ArrayList<Integer>();
    {
        for (int i = 0; i < 5; ++i)
            permInputs.add(i * i + 7 * i + 1);
        permInputs.add(9); // Have 9 appear twice in our little tests
        Collections.sort(permInputs);
    }

    private List<List<Integer>> allPermutations = allPermutations(permInputs);

    public void setUp() {
        pq0 = makePQueue();
        pq1 = makePQueue();
        pq1.insert(new TestElement(3));
    }

    /**
     * Trivial tests of the size() method.
     * 
     */
    public void testSize() {
        assertTrue(pq0.size() == 0);
        assertTrue(pq1.size() == 1);
    }

    private void opShouldThrow(boolean doInsert, PQueue p, PQAble elt,
            Class<? extends Exception> shouldThrow) {
        try {
            if (doInsert)
                p.insert(elt);
            else {
                if (!p.isAdvanced())
                    return; // No throw if no delete..
                p.delete(elt);
            }
            fail((doInsert ? "insert" : "delete") + "(" + elt
                    + ") failed to throw " + shouldThrow);
        } catch (Exception e) {
            assertEquals(shouldThrow, e.getClass());
        }
    }

    /**
     * Try to ensure that insert() throws the contracted exceptions.
     */
    public void testInsertExceptions() {
        opShouldThrow(true, pq0, null, NullPointerException.class);
        opShouldThrow(true, pq1, null, NullPointerException.class);

        PQAble p1 = new TestElement(1);
        pq0.insert(p1);

        opShouldThrow(true, pq0, p1, IllegalStateException.class);
        opShouldThrow(true, pq1, p1, IllegalStateException.class);

        PQAble p2 = new TestElement(1); // same priority..
        pq0.insert(p2);

        opShouldThrow(true, pq0, p1, IllegalStateException.class);
        opShouldThrow(true, pq1, p1, IllegalStateException.class);

        opShouldThrow(true, pq0, p2, IllegalStateException.class);
        opShouldThrow(true, pq1, p2, IllegalStateException.class);
    }

    /**
     * Test inserting 100 elements and checking the resulting size().
     * 
     */
    public void testInsert() {
        assertTrue(pq0.size() == 0);
        for (int i = 1; i < 100; ++i) {
            pq0.insert(new TestElement(i));
            assertTrue(pq0.size() == i);
        }
    }

    /**
     * For all possible permutations of a small set of test numbers, insert the
     * set of numbers, and then check that remove() them produces elements with
     * the correctly sorted priorities.
     * 
     */
    public void testRemove() {
        for (List<Integer> li : allPermutations)
            testRemovePerm(li);
    }

    private void testRemovePerm(List<Integer> perm) {
        assertTrue("" + perm, pq0.size() == 0);

        for (Integer i : perm)
            pq0.insert(new TestElement(i));

        for (Integer i : permInputs) {
            TestElement te = (TestElement) pq0.remove();
            assertNotNull("" + perm, te);
            assertEquals("" + perm, i.intValue(), te.getPriority());
        }
        assertTrue("" + perm, pq0.size() == 0);
    }

    /**
     * Try to ensure that delete() throws the contracted exceptions. Is
     * effectively a no-op if PQueue-under-test does not support delete()
     */
    public void testDeleteExceptions() {
        opShouldThrow(false, pq0, null, NullPointerException.class);

        PQAble in0 = new TestElement(1);
        opShouldThrow(false, pq0, in0, IllegalStateException.class);
        opShouldThrow(false, pq1, in0, IllegalStateException.class);

        pq0.insert(in0);
        opShouldThrow(false, pq1, in0, IllegalStateException.class);

        pq0.delete(in0);
        opShouldThrow(false, pq0, in0, IllegalStateException.class);
        opShouldThrow(false, pq1, in0, IllegalStateException.class);

        pq1.insert(in0);
        opShouldThrow(false, pq0, in0, IllegalStateException.class);

        pq1.delete(in0);
        opShouldThrow(false, pq0, in0, IllegalStateException.class);
        opShouldThrow(false, pq1, in0, IllegalStateException.class);
    }

    /**
     * For all possible permutations of a small set of test numbers, insert
     * elements containing those numbers, then delete() all but the last of
     * those elements, and then call remove() once and ensure that the returned
     * reference is identical to the last remaining element.
     * 
     * Is no-op if PQueue-under-test is not advanced.
     */
    public void testDelete() {
        if (!pq0.isAdvanced()) return;
        for (List<Integer> li : allPermutations)
            testDeletePerm(li);
    }

    private void testDeletePerm(List<Integer> perm) {
        assertTrue("" + perm, pq0.size() == 0);
        PQAble[] elts = new PQAble[perm.size()];

        for (int i = 0; i < perm.size(); ++i) {
            elts[i] = new TestElement(perm.get(i));
            pq0.insert(elts[i]);
        }

        // Delete n-1 elts, then remove the last and make sure it's right
        for (int i = 0; i < perm.size(); ++i) {
            if (i < perm.size() - 1) {
                pq0.delete(elts[i]);
            } else {
                TestElement te = (TestElement) pq0.remove();
                assertNotNull("" + perm, te);
                assertEquals("On " + perm + " expected " + elts[i]
                        + " but got " + te, elts[i], te); // We do equals here.
                // Test refs below
            }
            assertTrue("" + perm, pq0.size() == perm.size() - i - 1);
        }
        assertNull("" + perm, pq0.remove());
    }

    /**
     * Insert two bunches of element references containing overlapping
     * priorities, then delete all of one bunch of references and then ensure
     * that remove() brings out precisely all the other references in order.
     * 
     * No-op if PQueue-under-test does not support delete
     */
    public void testDeleteRefs() {
        if (!pq0.isAdvanced()) return;
        
        final int size = 100;
        PQAble[] goodrefs = new PQAble[size];
        PQAble[] spoilers = new PQAble[size];
        for (int i = 0; i < size; ++i)
            goodrefs[i] = new TestElement(i);
        for (int i = 0; i < size; ++i)
            spoilers[i] = new TestElement(size - i);

        // load up one pile of references
        for (int i = 0; i < size; ++i) {
            pq0.insert(spoilers[i]);
            assertTrue(pq0.size() == i + 1);
        }

        // now add the second bunch of references
        for (int i = 0; i < size; ++i) {
            pq0.insert(goodrefs[i]);
            assertTrue(pq0.size() == i + 1 + size);
        }

        // delete the first bunch of refs in mixed order
        for (int i = 0; i < size; ++i) {
            assertTrue(pq0.size() == 2 * size - i);
            if ((i & 1) == 0)
                pq0.delete(spoilers[i / 2]);
            else
                pq0.delete(spoilers[size / 2 + i / 2]);
        }

        // make sure the second bunch of refs still removes properly
        for (int i = 0; i < size; ++i) {
            PQAble ret = pq0.remove();
            assertNotNull(ret);
            assertTrue("Expected " + goodrefs[i] + " but got " + ret,
                    ret == goodrefs[i]);
        }
        assertTrue(pq0.size() == 0);
    }

    /**
     * Do some simple tests involving multiple pqueues simultaneously active,
     * moving elements back and forth between pqueues and such, to help ensure
     * there's no bogus static stuff going on in the PQueue-implementing
     * class-under-test.
     * 
     */
    public void testMultiples() {
        while (pq1.size() > 0)
            pq1.remove();
        for (int i = 100; i > 0; --i)
            pq0.insert(new TestElement(i));

        // Flow things back and forth a bit
        while (pq0.size() > 0)
            pq1.insert(pq0.remove());
        assertTrue(pq1.size() == 100);

        while (pq1.size() > 0)
            pq0.insert(pq1.remove());
        assertTrue(pq0.size() == 100);

        for (int i = 0; i < 100; ++i) {
            pq1.insert(pq0.remove());
            pq0.insert(pq1.remove());
        }
        assertTrue(pq0.size() == 100);

        for (int i = 1; i <= 100; ++i) {
            TestElement te = (TestElement) pq0.remove();
            assertNotNull(te);
            assertEquals(i, te.getPriority());
        }
        assertTrue(pq0.size() == 0);
    }

    /**
     * Make sure having several elements with identical priorities doesn't hurt
     * anything as far as insert/remove works.
     */
    public void testRepeatedElements() {
        for (int i = 100 - 1; i >= 0; --i) {
            pq0.insert(new TestElement(i / 10));
        }
        for (int i = 0; i < 100; ++i) {
            TestElement te = (TestElement) pq0.remove();
            assertNotNull(te);
            assertEquals(i / 10, te.getPriority());
        }
    }

    /**
     * Try some larger sequences of varied numbers, up to around a thousand
     * elements or so, to help flush out any bogus limited-size problems.
     * 
     */
    public void testSequences() {
        for (int n = (1 << 1); n <= (1 << 10); n <<= 1)
            for (Sequence s : EnumSet.allOf(Sequence.class))
                for (int i = 0; i < 2; ++i)
                    testSizeSequence(n, s, pq0.isAdvanced() && i == 1);
    }

    private void testSizeSequence(int n, Sequence s, boolean deleting) {
        TreeSet<Integer> nums = new TreeSet<Integer>(); // Need them sorted
        // Load up a table
        for (int i = 0; i < n; ++i) {
            int num = s.get(i, n);
            if (nums.contains(num))
                throw new IllegalStateException("duplicate " + num);
            nums.add(num);
        }

        // Load up pq0 in the order given by the sequence, perhaps with some
        // little deleting thrown in to help mess things up
        TestElement last = null;
        for (int i = 0; i < n; ++i) {
            TestElement te = new TestElement(s.get(i, n));
            pq0.insert(te);
            if (deleting) {
                if (last != null)
                    pq0.insert(last);
                pq0.delete(te);
                last = te;
            }
        }
        if (last != null)
            pq0.insert(last);

        // Drain it in priority order and check against sorted
        for (Integer i : nums) {
            TestElement te = (TestElement) pq0.remove();
            assertEquals(s + " with n=" + n, new Integer(te.getPriority()), i);
        }
        assertEquals(0, pq0.size());
    }
}
