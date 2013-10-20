package com.putable.pqueue.test;

import com.putable.pqueue.AbstractPQAble;
import com.putable.pqueue.PQAble;

/**
 * An AbstractPQAble for testing.
 *   
 * @author ackley
 */
class TestElement extends AbstractPQAble {
    private int deadline;

    @Override
    public int hashCode() {
        return deadline;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TestElement other = (TestElement) obj;
        if (deadline != other.deadline)
            return false;
        return true;
    }

    public TestElement(int deadline) {
        this.deadline = deadline;
    }

    public int getPriority() {
        return deadline;
    }

    public int compareTo(PQAble o) {
        TestElement t = (TestElement) o; // Or throw ClassCastException
        if (this.deadline < t.deadline)
            return -1;
        if (this.deadline > t.deadline)
            return +1;
        return 0;
    }

    public String toString() {
        return String.format("TestElement@%x[prio=%d]",super.hashCode(),getPriority());
    }
}