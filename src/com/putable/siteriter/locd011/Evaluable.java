package com.putable.siteriter.locd011;

import java.util.Random;

/**
 * Allow us to evaluate and expand our generated page
 * 
 * @author David
 */
public interface Evaluable
{
    /**
     * Evaluate the current Evaluable and handle recursive evaluate calls
     * @param selector
     * @return
     */
    public String evaluate(Random selector);

    /**
     * Evaluate the current Evaluable using a given choice, and handle recursive evaluate calls
     * @param selector
     * @param index
     * @return
     */
    public String evaluate(Random selector, int index);

    /**
     * Return the Symbol that this Evaluable represents
     * @return
     */
    public Symbol getSymbol();
}