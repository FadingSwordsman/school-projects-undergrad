package com.putable.timing;

/**
 * A base class for building classes that test operations for their BigO speed.
 * 
 * @author ackley
 * @version 1.0
 */
public abstract class AbstractBigOTest {
    /**
     * A volatile variable used in attempts to defeat 'helpful' compiler
     * optimizations
     */
    protected volatile int dontOptimizeMeBro;

    private BigO speed;
    private int minN;
    private int tests;
    private int seconds;
    /** typical maxseconds is {@value} */
    public static final int maxseconds = 1 + 3;

    /**
     * Create a BigOTest for an operation that is expected to run in 'speed'
     * time.
     * 
     * @param speed
     *            The asymptotic time efficiency that 'doIt' is expected to
     *            meet, for a given problem size.
     * @param minN
     *            The smallest problem size to be tested. Larger tests are
     *            always powers of 2 times this minimum size.
     * @param tests
     *            The number of tests to perform total, each at double the
     *            problem size of the last. The largest problem size tested will
     *            be minN&lt;&lt;tests.
     * @param seconds
     *            The approximate number of wallclock seconds to consume <i>per
     *            test</i>. The overall time taken by a call to assertSpeed will
     *            be something <i>greater</i> than tests*seconds, because new
     *            iterations each test are initiated as long as any time is
     *            remaining, so the final iteration of each test may run beyond
     *            the allotted time bound.
     * @throws IllegalArgumentException
     *             if seconds is less than 2, minN is less than 1, tests is less
     *             than 1, or minN&lt;&lt;tests is too big to fit in an int.
     * 
     */
    public AbstractBigOTest(BigO speed, int minN, int tests, int seconds) {
        if (minN < 1)
            throw new IllegalArgumentException("Need at least 1 minN");
        if (tests < 1)
            throw new IllegalArgumentException("Need at least 1 test");
        if (seconds <= 1)
            throw new IllegalArgumentException("Need at least 2 seconds");
        if ((minN << tests) < minN)
            throw new IllegalArgumentException("Too many tests, size wrapped");

        this.speed = speed;
        this.minN = minN;
        this.tests = tests;
        this.seconds = seconds;
    }

    /**
     * Called to set up a problem instance of size n. Concrete subclasses of
     * BigOTest must implement this in such a way that it can be called
     * repeatedly with the same and/or different values of n on the same
     * BigOTest instance, without interference. The time taken by calls to
     * reset() is <i>not</i> considered part of the testing time and doesn't
     * impact whether assertSpeed is successful at any given BigO. reset() time
     * is billed to the Timer.SETUP category in the timer data.
     * 
     * @param n
     *            The size of the problem instance to create.
     */
    public abstract void reset(int n);

    /**
     * Called to perform the operation under test. Concrete subclasses of
     * BigOTest must implement this in such a way that calling doIt 'n' times
     * will actually take some detectable and reasonable amount of time; for
     * very simple, fast operations, for example, it may be necessary to repeat
     * the operation a large constant number of times inside each call to doIt,
     * to drive the timing granularity up to a statistically detectable level.
     * By contrast, if calling doIt can be extremely time-consuming even when
     * implemented appropriately, then the values of 'minN', 'tests', and/or
     * 'seconds' supplied to the constructor may need to be adjusted so that at
     * least a couple sets of 'n' doIt calls can be completed before time runs
     * out.
     * 
     * @param iteration
     *            The current iteration number, which will vary from 0 to n-1,
     *            where n was the size passed to the most recent prior call to
     *            reset(). doIt implementations may use or ignore this value as
     *            convenient.
     */
    public abstract void doIt(int iteration);

    /**
     * Compute the average time in seconds to perform one 'doIt' operation on a
     * problem instance of size n
     * 
     * @param n
     *            The size of the problem instance (which is also the number of
     *            trials performed on the instance per 'timeOne' call).
     * @return the computed average seconds to do one 'doIt' operation
     */
    private final double timeIt(int n) {
        int totalSeconds = seconds;
        int sleepTime = 1;
        totalSeconds -= sleepTime;
        if (totalSeconds <= 0)
            throw new IllegalArgumentException("Not enough time allowed");

        long totalNanos = nanos(totalSeconds);
        long nanosWarmup = timeOne(n, Timer.WARMUP); // Blow off a round
        sleep(sleepTime); // Wait a bit for possible JITing to settle

        if (nanosWarmup >= totalNanos)
            throw new BigO.OperationTooSlow("Took more than " + totalSeconds
                    + " seconds warming up size " + n);
        totalNanos -= nanosWarmup;

        long nanosTesting = 0;
        long nanosSetup = 0;
        int totalIterations = 0;
        while (nanosTesting + nanosSetup < totalNanos) {
            long prevSetup = Timer.SETUP.getTotalTime();
            nanosTesting += timeOne(n, Timer.TEST); // Do 'n' doIt's
            nanosSetup += Timer.SETUP.getTotalTime() - prevSetup;
            totalIterations++;
        }
        if (totalIterations < 3) {
            System.err.println("Warning: Low iteration count ("
                    + totalIterations + ") for " + this.getClass().toString()
                    + " on size " + n);
        }
        return seconds(nanosTesting) / totalIterations / n;
    }

    /**
     * Perform the tests and gather timing data. This method will generally take
     * more than (tests*seconds) seconds to complete, for the 'tests' and
     * 'seconds' values specified in the constructor. The method simply returns
     * if the measured times are compatible with the BigO speed specified in the
     * constructor, otherwise throws some kind of exception (see below) if there
     * was a problem.
     * 
     * <p>
     * Note that trying to do this kind of timing test in Java is fraught with
     * difficulties than can be only partially worked around. It <i>is</i>
     * possible that code which by analysis meets a given BigO specification
     * might fail the corresponding test here, and it <i>is</i> possible that
     * code which by analysis does <i>not</i> meet a given BigO specification
     * might nonetheless pass the corresponding test here.
     * 
     * <p>
     * <b><i>YOUR MILEAGE MAY VARY.</i></b>
     * 
     * <p>
     * For example, Java does not provide any builtin facilities for measuring
     * run time, only wall clock time, and to be portable this code just lives
     * with that restiction, so these timing results can be strongly affected by
     * other threads, processes, and users running on the same machine; it is up
     * to the user to find a 'quiet place' to run these tests.
     * 
     * <p>
     * Similarly, a JVM may in principle choose to perform garbage collection
     * and/or 'Just In Time' code compilation at times that cannot be completely
     * controlled for or predicted by ordinary portable Java code such as this.
     * This code makes efforts to pull those factors apart -- deliberately
     * invoking garbage collection around tests, and sleeping for a bit at
     * moments that might be candidates for JIT compilation, but these are
     * necessarily approximate hacks.
     * 
     * @throws BigO.OperationTimeMismatch
     *             This exception is thrown if the measured times <i>seem</i>
     *             incompatible with the specified BigO class.
     * 
     * @throws BigO.OperationTooSlow
     *             This exception is thrown if the 'doit' operation took so much
     *             time (at some tested size) that there was no time left to do
     *             any rounds of testing after the one 'warmup' round was
     *             completed.
     * 
     * @throws BigO.OperationTooFast
     *             This exception is thrown if the 'doIt' operation at some size
     *             was so fast that the wall clock time apparently didn't
     *             advance at all, making any kind of plausible timing
     *             measurement impossible.
     */
    public void assertSpeed() {
        Sample[] samples = timeSizes();

        double avg = 0;
        for (int i = 0; i < samples.length; ++i)
            avg += speed.norm(samples[i]);
        avg /= samples.length;

        double window = 5.0;
        for (int i = 0; i < samples.length; ++i) {
            double work = speed.norm(samples[i]);
            double norm = work / avg;
            if (norm < 1.0 / window || norm > window / 1.0) {
                String msg = "Not " + speed + ": " + norm + "=" + work + "/"
                        + avg + "\n";
                for (int j = 0; j < samples.length; ++j)
                    msg += samples[j].toString() + " " + speed.norm(samples[j])
                            + "\n";
                throw new BigO.OperationTimeMismatch(msg);
            }
        }
    }

    private static double seconds(long nanos) {
        return nanos / 1000000000.0;
    }

    private static long nanos(double seconds) {
        return (long) (seconds * 1000000000.0);
    }

    private long maxMem = 0;

    private long getFreeMem() {
        long freeMem = Runtime.getRuntime().freeMemory();
        if (freeMem > maxMem)
            maxMem = freeMem;
        return freeMem;
    }

    private void maybeGc() {
        long freeMem = getFreeMem();
        if (freeMem < maxMem / 5) {
            Timer.push(Timer.GC);
            System.gc();
            getFreeMem();
            Timer.pop();
        }
    }

    private void sleep(int seconds) {
        Timer.push(Timer.SLEEP);
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            /* Ignore interrupt. Who woke us? */
        }
        Timer.pop();
    }

    /**
     * @param n
     * @return total time (in nanoseconds) to perform n 'doIt's. on an instance
     *         of size 'n'
     */
    private long timeOne(int n, Timer billTo) {
        Timer.push(Timer.SETUP);
        reset(n); // Set up an instance of size n
        Timer.pop();
        maybeGc();

        long startTotalTime = Timer.push(billTo);
        for (int i = 0; i < n; ++i)
            doIt(i); // Run n trials on it
        long endTotalTime = Timer.pop();
        if (endTotalTime == startTotalTime)
            throw new BigO.OperationTooFast("Size " + n + " took no time");

        maybeGc();

        return endTotalTime - startTotalTime;
    }

    private Sample[] timeSizes() {
        Sample[] samples = new Sample[tests];
        int steps = minN;
        for (int i = 0; i < tests; ++i, steps <<= 1) {
            samples[i] = new Sample();
            samples[i].setN(steps);
        }
        for (int i = 0; i < tests; ++i)
            samples[i].setT(timeIt(samples[i].getN()));

        System.out.println(this.getClass() + "\n" + Timer.timeData());
        return samples;
    }

}