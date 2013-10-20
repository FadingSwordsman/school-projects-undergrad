package com.putable.timing;

/**
 * A representation of some of the familiar BigO complexity categories.
 * 
 * @author ackley
 * @version 1.0
 */
public enum BigO {
    /** For operations that take the same time no matter what 'n' */
    CONSTANT  { public double expect(int n) { return 1; } },

    /** For operation times that grow as the logarithm of 'n' */
    LOG       { public double expect(int n) { return Math.log((double) n); } },

    /** For operation times that grow proportionately to 'n' */
    LINEAR    { public double expect(int n) { return n; } },

    /** For operation times that grow as n*log(n) */
    NLOGN     { public double expect(int n) { return n * Math.log((double) n); } },

    /** For operation times that grow proportionately to 'n' squared */
    QUADRATIC { public double expect(int n) { return n * n; } };

    /**
     * Normalize a Sample given a particular BigO assumption. If the Sample's t
     * is consistent with the Sample's BigO(n) under consideration, the return
     * values from norm(Sample) should be relatively constant no matter what
     * problem size is in the Sample.
     * 
     * @param s
     *            The sample to normalize
     * @return the would-be-constant amount of 'work' given the assumed BigO
     */
    public double norm(Sample s) {
        return s.getT() / expect(s.getN());
    }

    /**
     * Give the amount of 'time' (in arbitrary units) that performing one trial
     * of work should take, assuming the performance is BigO time for applied to
     * n cases.
     * 
     * @param n
     *            the size of the problem
     * @return the expected time complexity for size 'n'.
     */
    public abstract double expect(int n);

    /**
     * This exception is thrown by BigO.assertSpeed() if the measured times
     * <i>seem</i> incompatible with the specified BigO class. Note that this
     * exception can be thrown even if the doIt operation seems to be running
     * faster than the given BigO, not just if it seems too slow. If an
     * operation is supposed to be BigO.LINEAR, for example, but the measured
     * timings seems to be constant and independent of the problem size, this
     * exception can be thrown.
     */
    public static class OperationTimeMismatch extends RuntimeException {
        public static final long serialVersionUID = 1L;

        public OperationTimeMismatch(String msg) {
            super(msg);
        }
    }

    /**
     * If this exception is thrown by BigOTest.assertSpeed, either the code
     * being tested simply really needs to be sped up (probably with but
     * possibly without changing its analytic BigO), or at least more liberal
     * parameters (smaller minN and/or tests, and/or larger seconds) need to be
     * provided to the BigOTest constructor.
     * 
     */
    public static class OperationTooSlow extends RuntimeException {
        public static final long serialVersionUID = 1L;

        public OperationTooSlow(String msg) {
            super(msg);
        }
    }

    /**
     * If this exception is thrown by BigOTest.assertSpeed -- assuming the code
     * under test actually <i>functions correctly</i> -- then most likely the
     * 'doIt' method needs to be rewritten to take more time (without altering
     * its BigO properties, of course), or perhaps the minN test size in the
     * BigOTest constructor could just be increased (assuming the BigO in
     * question is not BigO.CONSTANT).
     */
    public static class OperationTooFast extends RuntimeException {
        public static final long serialVersionUID = 1L;

        public OperationTooFast(String msg) {
            super(msg);
        }
    }
}
