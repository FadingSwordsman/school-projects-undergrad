package com.putable.timing;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * A representation of the various places where program runtime can be consumed.
 * 
 * @author ackley
 * @version 1.0
 */
enum Timer {
    GC, SETUP, WARMUP, TEST, SLEEP, LOST;
    private long totalTime = 0;
    private long lastStart = 0;
    private static ArrayList<Timer> stack = new ArrayList<Timer>();

    private static Timer topStack() {
        return stack.get(stack.size() - 1);
    }

    private static void popStack() {
        stack.remove(stack.size() - 1);
    }

    private static void pushStack(Timer t) {
        stack.add(t);
    }

    private static long billToNow() {
        Timer top = topStack();
        long now = System.nanoTime();
        top.totalTime += now - top.lastStart;
        top.lastStart = now;
        return now;
    }

    /**
     * Push a backstop entry onto the timer stack. If we pop back down to this
     * 'LOST' entry, something has gone wrong.
     */
    static {
        stack.add(LOST);
        LOST.lastStart = System.nanoTime();
    }

    /**
     * Find out how much total time has <i>previously</i> been billed to this
     * timer. Note: This method does <i>not</i> update the returned value to
     * account for time since the last Timer.push or Timer.pop operation, so if
     * this timer is running (meaning it is currently the top of the Timer
     * stack), then calling getTotalTime() just before and just after a
     * Timer.pop may return different values.
     * 
     * @return The total time previously billed to this timer.
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * Push t on top of the Timer stack. The total time used by the previous top
     * of stack is updated, and then t is pushed onto the stack, so until the
     * next Timer operation, the intervening time will be 'billed' to Timer t.
     * 
     * @param t
     *            The specific Timer to push onto the stack
     * @return The total time used so far by Timer t
     */
    public static long push(Timer t) {
        t.lastStart = billToNow();
        pushStack(t);
        return t.totalTime;
    }

    /**
     * Pop the top of the Timer stack. The total time used by the current top of
     * the stack is updated, and then the stack is popped, so until the next
     * Timer operation the intervening time will be 'billed' to the new
     * top-of-stack Timer exposed by the pop.
     * 
     * @return The total time used so far by the new top of stack
     * @throws IllegalStateException
     *             if an attempt is made to pop the 'LOST' timer that lives at
     *             the very bottom of the stack.
     */
    public static long pop() {
        if (stack.size() <= 1)
            throw new IllegalStateException("Overpopped");
        long now = billToNow();
        long ret = topStack().totalTime;
        popStack();
        topStack().lastStart = now;
        return ret;
    }

    /**
     * Say where the time has gone.
     * 
     * @return A multiline String with a line for each category of time billing,
     *         and a total time at the bottom.
     */
    public static String timeData() {
        long totalAll = 0;
        for (Timer t : EnumSet.allOf(Timer.class)) {
            totalAll += t.totalTime;
        }
        StringBuffer ret = new StringBuffer();
        for (Timer t : EnumSet.allOf(Timer.class)) {
            ret.append(String.format("%s %6.2f%%\n", t.timeString(), 100.0
                    * t.totalTime / totalAll));
        }
        ret.append(format("Total", totalAll) + "\n");
        return ret.toString();
    }

    private String timeString() {
        return format(this.toString(), totalTime);
    }

    private static String format(String name, long time) {
        return String.format("%8s %9.4f", name, time / 1000000000.0);
    }
}