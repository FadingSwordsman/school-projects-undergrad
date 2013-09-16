package com.putable.siteriter.locd011;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.putable.siteriter.SDLParseException;

/**
 * An enum to help our definition of valid parsings
 * 
 * @author David
 */
public enum ParseConstruction
{
    PROGRAM(programParser()), RULE(ruleParser()), HEAD(headParser()), CHOICE(choiceParser()), SEQUENCE(sequenceParser());
    private Parser parser;

    /**
     * Begins a parsing of the given token stream, and returns a list of Symbols
     * in the form of Evaluables.
     * 
     * @param tokenValuePairs
     * @param parser
     * @return
     * @throws SDLParseException
     */
    public static Iterable<Evaluable> beginParse(Iterable<Tuple<Token, String>> tokenValuePairs, SDLParserImpl parser) throws SDLParseException
    {
	return PROGRAM.parser.parse(tokenValuePairs, parser);
    }

    private ParseConstruction(Parser parser)
    {
	this.parser = parser;
    }

    private interface Parser
    {
	public Iterable<Evaluable> parse(Iterable<Tuple<Token, String>> tokenValuePairs, SDLParserImpl parser) throws SDLParseException;
    }

    private static Parser programParser()
    {
	return new Parser()
	{
	    public Iterable<Evaluable> parse(Iterable<Tuple<Token, String>> tokenValuePairs, SDLParserImpl parser) throws SDLParseException
	    {
		List<Evaluable> rules = new LinkedList<Evaluable>();
		List<Tuple<Token, String>> nextRule = new LinkedList<Tuple<Token, String>>();
		for (Tuple<Token, String> nextToken : tokenValuePairs)
		{
		    Token tokenType = nextToken.getFirst();
		    nextRule.add(nextToken);
		    if (tokenType == Token.SEMICOLON)
		    {
			// We have a rule:
			for (Evaluable rule : ParseConstruction.RULE.parser.parse(nextRule, parser))
			    rules.add(rule);
			nextRule = new LinkedList<Tuple<Token, String>>();
		    }
		}
		if (nextRule.get(0).getFirst() == Token.EOI && rules.size() > 0)
		    return rules;
		throw new SDLParseException("Invalid Parse. Expected SEMICOLON before EOI.");
	    }

	};
    }

    private static Parser ruleParser()
    {
	return new Parser()
	{
	    public Iterable<Evaluable> parse(Iterable<Tuple<Token, String>> tokenValuePairs, SDLParserImpl parser) throws SDLParseException
	    {
		Iterator<Tuple<Token, String>> iterator = tokenValuePairs.iterator();
		List<Tuple<Token, String>> portion = new LinkedList<Tuple<Token, String>>();
		boolean headFull = false;
		List<Evaluable> ruleDefinition = new LinkedList<Evaluable>();
		Evaluable ruleName = null;
		Evaluable ruleBody = null;
		// Setup and call the HEAD parser
		while (!headFull && iterator.hasNext())
		{
		    Tuple<Token, String> nextToken = iterator.next();
		    if (nextToken.getFirst() == Token.EQUAL)
		    {
			headFull = true;
			Iterable<Evaluable> headName = ParseConstruction.HEAD.parser.parse(portion, parser);
			// Should be exactly one, enforced by the HEAD parser
			int x = 0;
			for (Evaluable name : headName)
			{
			    ruleName = name;
			    x++;
			}
			if (x > 1)
			    throw new SDLParseException("Found multiple name tokens for one rule.");
			portion = new LinkedList<Tuple<Token, String>>();
		    }
		    else
			portion.add(nextToken);
		}
		if (portion.size() != 0)
		    throw new SDLParseException("No valid rule head found!");
		Iterable<Evaluable> choices;
		while (iterator.hasNext())
		{
		    Tuple<Token, String> nextToken = iterator.next();
		    if (nextToken.getFirst() == Token.SEMICOLON)
		    {
			choices = ParseConstruction.CHOICE.parser.parse(portion, parser);
			int x = 0;
			for (Evaluable choice : choices)
			{
			    ruleBody = choice;
			    x++;
			}
			if (x < 1)
			    throw new SDLParseException("Parse failure! Multiple choice parses found for the same rule.");
		    }
		    else
			portion.add(nextToken);
		}
		if (portion.size() == 0)
		    ruleBody = blankSequenceEvaluable();
		Evaluable completedRule = ruleEvaluable(ruleBody, ruleName.getSymbol(), parser);
		ruleDefinition = new LinkedList<Evaluable>();
		ruleDefinition.add(completedRule);
		return ruleDefinition;
	    }
	};
    }

    /**
     * Create a parser that recognizes a HEAD construction, and constructs an
     * appropriate Evaluable
     * 
     * @return
     */
    private static Parser headParser()
    {
	return new Parser()
	{
	    public Iterable<Evaluable> parse(Iterable<Tuple<Token, String>> tokenValuePairs, SDLParserImpl parser) throws SDLParseException
	    {
		Iterator<Tuple<Token, String>> iterator = tokenValuePairs.iterator();
		List<Evaluable> rule = new ArrayList<Evaluable>();
		// Variables for building our Symbol
		String name;
		boolean isAlternateStartSymbol = false;
		String selector = null;
		// We expect between 1 and 4 valuePairs. 0 means an equal
		// was entered first.
		if (!iterator.hasNext())
		    throw new SDLParseException("Expected NAME or COLON. Received neither.");
		Tuple<Token, String> nextToken = iterator.next();
		List<Token> tokens = new ArrayList<Token>();
		// If we started with a colon, the name is also a selector:
		if (nextToken.getFirst() == Token.COLON)
		{
		    if (!iterator.hasNext())
			throw new SDLParseException("Expected NAME, but was not present!");
		    nextToken = iterator.next();
		    if (nextToken.getFirst() != Token.NAME)
			throw new SDLParseException("Expected NAME, but received " + nextToken.getFirst());
		    isAlternateStartSymbol = true;
		    tokens.add(nextToken.getFirst());
		}
		// Set our name for the rule:
		name = nextToken.getSecond();
		tokens.add(nextToken.getFirst());
		// We may have a selector after the name of the variable:
		if (iterator.hasNext())
		{
		    nextToken = iterator.next();
		    if (nextToken.getFirst() != Token.COLON)
			throw new SDLParseException("Expected COLON or EQUAL. Received " + nextToken.getFirst());
		    if (!iterator.hasNext())
			throw new SDLParseException("Expected NAME. Received nothing.");
		    tokens.add(nextToken.getFirst());
		    nextToken = iterator.next();
		    if (nextToken.getFirst() != Token.NAME)
			throw new SDLParseException("Expected NAME. Received " + nextToken.getFirst());
		    selector = nextToken.getSecond();
		    tokens.add(nextToken.getFirst());
		}
		if (iterator.hasNext())
		{
		    nextToken = iterator.next();
		    throw new SDLParseException("Expected EQUAL. Received " + nextToken.getFirst());
		}
		Symbol createdSymbol = headSymbol(name, isAlternateStartSymbol, selector, tokens);
		Evaluable eval = headEvaluable(createdSymbol);
		rule.add(eval);
		return rule;
	    }
	};
    }

    /**
     * Create a parser that recognizes a CHOICE construction, constructs an
     * appropriate Evaluable, and returns it to the RULE parser
     * 
     * @return
     */
    private static Parser choiceParser()
    {
	return new Parser()
	{
	    public Iterable<Evaluable> parse(Iterable<Tuple<Token, String>> tokenValuePairs, SDLParserImpl parser) throws SDLParseException
	    {
		Iterator<Tuple<Token, String>> iterator = tokenValuePairs.iterator();
		List<Evaluable> choices = new ArrayList<Evaluable>();
		List<Tuple<Token, String>> nextChoice = new LinkedList<Tuple<Token, String>>();
		List<Token> tokens = new ArrayList<Token>();
		while (iterator.hasNext())
		{
		    Tuple<Token, String> nextToken = iterator.next();
		    if (nextToken.getFirst() == Token.BAR)
		    {
			Iterable<Evaluable> sequence;
			// The special case of having a leading bar
			if (nextChoice.isEmpty())
			{
			    List<Evaluable> sequencer = new LinkedList<Evaluable>();
			    sequencer.add(blankSequenceEvaluable());
			    sequence = sequencer;
			}
			else
			{
			    sequence = ParseConstruction.SEQUENCE.parser.parse(nextChoice, parser);
			}
			// There should be only one!
			for (Evaluable evaluable : sequence)
			    choices.add(evaluable);
			// In the garbage collector, we trust!
			nextChoice = new LinkedList<Tuple<Token, String>>();
		    }
		    else if (!iterator.hasNext())
		    {
			Iterable<Evaluable> sequence;
			nextChoice.add(nextToken);
			sequence = ParseConstruction.SEQUENCE.parser.parse(nextChoice, parser);
			// There should be only one!
			for (Evaluable evaluable : sequence)
			    choices.add(evaluable);
			// In the garbage collector, we trust!
			nextChoice = new LinkedList<Tuple<Token, String>>();
		    }
		    else
			nextChoice.add(nextToken);
		    tokens.add(nextToken.getFirst());
		}
		Evaluable finishedChoice = choiceEvaluable(choices, parser, tokens);
		List<Evaluable> choiceList = new LinkedList<Evaluable>();
		choiceList.add(finishedChoice);
		return choiceList;
	    }
	};
    }

    /**
     * Create a parser that recognizes a SEQUENCE construction, constructs an
     * appropriate Evaluable, and returns it to the CHOICE parser
     * 
     * @return
     */
    private static Parser sequenceParser()
    {
	return new Parser()
	{
	    public Iterable<Evaluable> parse(Iterable<Tuple<Token, String>> tokenValuePairs, SDLParserImpl parser) throws SDLParseException
	    {
		final List<Evaluable> sequence = new ArrayList<Evaluable>();
		for (Tuple<Token, String> nextToken : tokenValuePairs)
		{
		    if (nextToken.getFirst() == Token.DLITERAL || nextToken.getFirst() == Token.SLITERAL)
		    {
			String value = nextToken.getSecond();
			Evaluable literal = literalEvaluable(value);
			sequence.add(literal);
		    }
		    else if (nextToken.getFirst() == Token.NAME)
		    {
			Evaluable name = nameEvaluable(nextToken.getSecond(), parser);
			sequence.add(name);
		    }
		    else
			throw new SDLParseException("Expected DLITERAL, SLITERAL, or NAME. Received " + nextToken.getFirst());
		}
		Evaluable sequenceEvaluable = sequenceEvaluable(sequence);
		List<Evaluable> finishedSequence = new ArrayList<Evaluable>();
		finishedSequence.add(sequenceEvaluable);
		return finishedSequence;
	    }
	};
    }

    /**
     * The Evaluable for a SEQUENCE, which just returns a list of sequential
     * values
     * 
     * @param sequencer
     * @return
     */
    private static Evaluable sequenceEvaluable(final Iterable<Evaluable> sequencer)
    {
	return new Evaluable()
	{
	    @Override
	    public String evaluate(Random selector)
	    {
		String evaluated = "";
		for (Evaluable next : sequencer)
		    evaluated += next.evaluate(selector);
		return evaluated;
	    }

	    @Override
	    public String evaluate(Random selector, int index)
	    {
		return evaluate(selector);
	    }

	    @Override
	    public Symbol getSymbol()
	    {
		return null;
	    }
	};
    }

    /**
     * The Evaluable for a D or SLITERAL, which just evaluates to a string
     * 
     * @param output
     * @return
     */
    private static Evaluable literalEvaluable(final String output)
    {
	return new Evaluable()
	{
	    @Override
	    public String evaluate(Random selector)
	    {
		return output.substring(1, output.length() - 1);
	    }

	    @Override
	    public String evaluate(Random selector, int index)
	    {
		return evaluate(selector);
	    }

	    @Override
	    public Symbol getSymbol()
	    {
		return null;
	    }
	};
    }

    /**
     * The Evaluable for a RULE, which combines a HEAD and SEQUENCE Evaluable
     * 
     * @param ruleBody
     * @param ruleSymbol
     * @param parser
     * @return
     */
    private static Evaluable ruleEvaluable(final Evaluable ruleBody, final Symbol ruleSymbol, final SDLParserImpl parser)
    {
	return new Evaluable()
	{
	    @Override
	    public String evaluate(Random selector)
	    {
		// Handle C.3.4.2.1.2.1
		if (ruleSymbol.getSelector() != null)
		{
		    // C.3.4.2.1.2.2: Selector is looked up, and set if it isn't
		    // found.
		    String ruleSelector = ruleSymbol.getSelector();
		    Integer select = parser.getSelector(ruleSelector);
		    if (select == -1)
		    {
			// Between 0 and MAX_INT - 1, C.3.4.2.1.2.2.1
			select = selector.nextInt(Integer.MAX_VALUE);
			parser.setSelector(ruleSymbol.getSelector(), select);
		    }
		    return evaluate(selector, select);
		}
		return ruleBody.evaluate(selector);
	    }

	    @Override
	    public String evaluate(Random selector, int index)
	    {
		return ruleBody.evaluate(selector, index);
	    }

	    @Override
	    public Symbol getSymbol()
	    {
		return ruleSymbol;
	    }
	};
    }

    /**
     * The Evaluable for a head, which creates the majority of the Symbol
     * 
     * @param createdSymbol
     * @return
     */
    private static Evaluable headEvaluable(final Symbol createdSymbol)
    {
	return new Evaluable()
	{
	    @Override
	    public String evaluate(Random selector)
	    {
		return createdSymbol.getName();
	    }

	    @Override
	    public String evaluate(Random selector, int index)
	    {
		return evaluate(selector);
	    }

	    @Override
	    public Symbol getSymbol()
	    {
		return createdSymbol;
	    }
	};
    }

    /**
     * The constructor for the HEAD symbol
     * 
     * @param name
     * @param isAlternateStart
     * @param selectorName
     * @param tokens
     * @return
     */
    private static Symbol headSymbol(final String name, final boolean isAlternateStart, final String selectorName, final List<Token> tokens)
    {
	return new Symbol()
	{
	    private List<Evaluable> choiceSet;

	    @Override
	    public String getName()
	    {
		return name;
	    }

	    @Override
	    public String getSelector()
	    {
		return selectorName;
	    }

	    @Override
	    public boolean isAlternateStartString()
	    {
		return isAlternateStart;
	    }

	    @Override
	    public List<Evaluable> getChoiceSet()
	    {
		return choiceSet;
	    }

	    @Override
	    public List<Token> getTokens()
	    {
		return tokens;
	    }
	};
    }

    private static Evaluable choiceEvaluable(final List<Evaluable> choices, final SDLParserImpl parser, final List<Token> tokens)
    {
	return new Evaluable()
	{
	    private List<Evaluable> choiceList = choices;
	    private Symbol symbol = new Symbol()
	    {
		@Override
		public String getName()
		{
		    return null;
		}

		@Override
		public String getSelector()
		{
		    return null;
		}

		@Override
		public List<Evaluable> getChoiceSet()
		{
		    return choices;
		}

		@Override
		public List<Token> getTokens()
		{
		    return tokens;
		}

		@Override
		public boolean isAlternateStartString()
		{
		    return false;
		}
	    };

	    @Override
	    public String evaluate(Random selector)
	    {
		int next = selector.nextInt(choiceList.size());
		return choiceList.get(next).evaluate(selector);
	    }

	    @Override
	    public String evaluate(Random selector, int index)
	    {
		int choice = index % choiceList.size();
		return choiceList.get(choice).evaluate(selector);
	    }

	    @Override
	    public Symbol getSymbol()
	    {
		return symbol;
	    }
	};
    }

    private static Evaluable blankSequenceEvaluable()
    {
	return new Evaluable()
	{
	    @Override
	    public String evaluate(Random selector)
	    {
		return "";
	    }

	    @Override
	    public String evaluate(Random selector, int index)
	    {
		return evaluate(selector);
	    }

	    @Override
	    public Symbol getSymbol()
	    {
		return null;
	    }
	};
    }

    private static Evaluable nameEvaluable(final String nameDefinition, final SDLParserImpl parser)
    {
	return new Evaluable()
	{
	    public String evaluate(Random r)
	    {
		Evaluable passOnResponsibility = parser.getDefinition(nameDefinition);
		if (passOnResponsibility != null)
		    return passOnResponsibility.evaluate(r);
		return nameDefinition + "?";
	    }

	    @Override
	    public String evaluate(Random selector, int index)
	    {
		return evaluate(selector);
	    }

	    @Override
	    public Symbol getSymbol()
	    {
		return null;
	    }
	};
    }
}