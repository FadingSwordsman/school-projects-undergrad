package com.putable.siteriter.locd011;

import java.util.List;

public interface Symbol
{
    /**
     * Return the name of this Symbol
     * 
     * @return
     */
    public String getName();

    /**
     * Return the selector of this Symbol
     * 
     * @return A String containing the Selector name, or null if there is none
     */
    public String getSelector();

    /**
     * Return the possible Evaluables this Symbol can evaluate to.
     * 
     * @return A List of Evaluables, one of which may be called if this Symbol
     *         were evaluated
     */
    public List<Evaluable> getChoiceSet();

    /**
     * Return whether this is an acceptable alternate start string
     * 
     * @return True if this is a marked alternate start string, false otherwise
     */
    public boolean isAlternateStartString();

    /**
     * Get the list of tokens that make up this symbol
     * 
     * @return
     */
    public List<Token> getTokens();
}