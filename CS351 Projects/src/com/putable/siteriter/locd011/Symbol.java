package com.putable.siteriter.locd011;

import java.util.List;
import java.util.Map;
import java.util.Random;

public interface Symbol
{
    /**
     * Return the name of this Symbol
     * @return
     */
    public String getName();

    /**
     * Return a boolean representing whether this Symbol can be used as an alternate start
     * @return
     */
    public boolean isAlternateStart();

    /**
     * Return the selector, if any, that this Symbol uses
     * @return
     */
    public String getSelector();

    /**
     * Return the choices available in this Symbol.
     * @return
     */
    public List<Symbol> getChoices();

    /**
     * Return the Choice at a certain index of this Symbol
     * @param index
     * @return
     */
    public Symbol getChoiceAt(int index);

    /**
     * Return the token at a given index in this Symbol
     * @param index
     * @return
     */
    public Token getTokenAt(int index);

    /**
     * Return the number of tokens that this Symbol represents
     * @return
     */
    public int getTokenLength();

    /**
     * Evaluate this token, and return a String determined by the inputs.
     * @param r
     * @param symbolTable
     * @param select
     * @return
     */
    public String evaluate(Random r, Map<String, Symbol> symbolTable, Map<String, Integer> select);
}