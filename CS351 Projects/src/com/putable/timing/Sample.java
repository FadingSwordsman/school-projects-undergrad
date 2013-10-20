package com.putable.timing;

/**
 * A tiny class to hold an association between a problem instance size
 * 'n' and the amount of time 't' it took to perform a single operation
 * on such a problem instance.
 * 
 * @author ackley
 * @version 1.0
 */
class Sample {
    /**
     * Size of the problem being operated on in this sample
     */
    private int n;
    /**
     * Average time per operation in this sample
     */
    private double t;

    /**
     * Return a fixed-width-formatted representation of the sample.
     */
    public String toString() {
        return String.format("%8d %10.7f", n, t);
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }
}