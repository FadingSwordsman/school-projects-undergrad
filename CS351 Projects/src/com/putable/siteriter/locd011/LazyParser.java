package com.putable.siteriter.locd011;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.putable.siteriter.SDLParseException;

public enum LazyParser
{
    RULE(ruleParser()),
    HEAD(headParser()),
    CHOICE(choiceParser()),
    SEQUENCE(sequenceParser());
    private Parser parser;

    public static Iterable<Symbolable> parse(Reader reader) throws IOException
    {
	Symbolable ruleSymbol = RULE.parser.parse(reader);
	List<Symbolable> rules = new LinkedList<Symbolable>();
	rules.add(ruleSymbol);
	return new LinkedList<Symbolable>();
    }

    private static boolean notComplete(Iterable<Tuple<LazyToken, String>> rule)
    {
	boolean complete = false;
	for (Tuple<LazyToken, String> next : rule)
	    complete = complete && next.getFirst() == LazyToken.EOI;
	return !complete;
    }

    private LazyParser(Parser parser)
    {
	this.parser = parser;
    }

    private Symbolable parseReader(Reader reader)
    {
	return parser.parse(reader);
    }

    private interface Parser
    {
	public Symbolable parse(Reader reader);
    }

    private static Parser ruleParser()
    {
	return new Parser()
	{
	    public Symbolable parse(Reader reader)
	    {
		Symbolable head = HEAD.parser.parse(reader);
		Symbolable choices = CHOICE.parser.parse(reader);
		return constructRuleSymbol(head, choices);
	    }
	};
    }

    private static Parser headParser()
    {
	return new Parser()
	{
	    @Override
	    public Symbolable parse(Reader reader)
	    {
		List<Tuple<LazyToken, String>> headValues;
		return null;
	    }
	};
    }

    private static Parser choiceParser()
    {
	return new Parser()
	{
	    @Override
	    public Symbolable parse(Reader reader)
	    {
		
		return null;
	    }
	};
    }

    private static Symbolable constructRuleSymbol(Symbolable head, Symbolable choices)
    {
	final String name = head.getName();
	final boolean isAlternateStart = head.isAlternateStart();
	final String selector = head.getSelector();
	
	final List<Symbolable> choiceList = choices.getChoices();
	
	final List<LazyToken> tokenList = new ArrayList<LazyToken>();
	//Aggregate the tokens, and add the ones we removed as a byproduct of parsing:
	for(int i = 0;  i < head.getTokenLength(); i++)
	    tokenList.add(head.getTokenAt(i));
	tokenList.add(LazyToken.EQUAL);
	for(int i = 0; i < choices.getTokenLength(); i++)
	    tokenList.add(head.getTokenAt(i));
	tokenList.add(LazyToken.SEMICOLON);
	
	return new Symbolable()
	{
	    private List<Symbolable> choices = choiceList;
	    private List<LazyToken> tokens = tokenList;

	    @Override
	    public String getName()
	    {
		return name;
	    }
	    
	    @Override
	    public boolean isAlternateStart()
	    {
		return isAlternateStart;
	    }

	    @Override
	    public String getSelector()
	    {
		return selector;
	    }

	    @Override
	    public Symbolable getChoiceAt(int index)
	    {
		return choices.get(index);
	    }

	    @Override
	    public LazyToken getTokenAt(int index)
	    {
		return tokens.get(index);
	    }

	    @Override
	    public int getTokenLength()
	    {
		return tokenList.size();
	    }

	    @Override
	    public String evaluate(Random r, Map<String, Symbolable> symbolTable, Map<String, Integer> select)
	    {
		//TODO: Implement the selector and choice interface
		return null;
	    }

	    @Override
	    public List<Symbolable> getChoices()
	    {
		return choices;
	    }
	};
    }

    private static Symbolable constructHead(Reader reader) throws IOException
    {
	// Grammar: [:]NAME[:NAME]
	List<Tuple<LazyToken, String>> tokens = new ArrayList<Tuple<LazyToken, String>>();
	List<Tuple<LazyToken, String>> nextTokens = getNextTokens(reader);
	tokens.addAll(nextTokens);
	
	int currentToken = 0;
	boolean alternateStartString = nextTokens.get(0).getFirst() == LazyToken.COLON;
	if(alternateStartString)
	{
	    currentToken++;
	    if(currentToken == tokens.size())
	    {
		nextTokens = getNextTokens(reader);
		tokens.addAll(nextTokens);
		currentToken = 0;
	    }
	}
	
	String symbolName;
	if(nextTokens.get(currentToken).getFirst() == LazyToken.NAME)
	{
	    symbolName = nextTokens.get(currentToken).getSecond();
	    currentToken++;
	}
	else
	    throw new SDLParseException("Expected NAME!");
	
	if(currentToken == nextTokens.size())
	{
	    nextTokens = getNextTokens(reader);
	    tokens.addAll(nextTokens);
	    currentToken = 0;
	}
	
	String selector = null;
	if (nextTokens.get(currentToken).getFirst() == LazyToken.COLON)
	{
	    currentToken++;
	    if (currentToken == nextTokens.size())
	    {
		nextTokens = getNextTokens(reader);
		tokens.addAll(nextTokens);
		currentToken = 0;
	    }
	    if(nextTokens.get(currentToken).getFirst() == LazyToken.NAME)
		selector = nextTokens.get(currentToken).getSecond();
	    else
		throw new SDLParseException("Expected matching NAME for associated COLON!");
	}
	
	return constructHeadSymbol(symbolName, alternateStartString, selector, tokens);
    }
    
    private static Symbolable constructHeadSymbol(final String name, final boolean alternateStartString, final String selector, final List<Tuple<LazyToken, String>> tokenRepresentation)
    {
	return new Symbolable()
	{
	    private List<Tuple<LazyToken, String>> tokens = tokenRepresentation;
	    
	    @Override
	    public String getName()
	    {
		return name;
	    }
	    
	    @Override
	    public boolean isAlternateStart()
	    {
		return alternateStartString;
	    }
	    
	    @Override
	    public String getSelector()
	    {
		return selector;
	    }

	    @Override
	    public List<Symbolable> getChoices()
	    {
		return null;
	    }

	    @Override
	    public Symbolable getChoiceAt(int index)
	    {
		return null;
	    }

	    @Override
	    public LazyToken getTokenAt(int index)
	    {
		return tokens.get(index).getFirst();
	    }

	    @Override
	    public int getTokenLength()
	    {
		return tokens.size();
	    }

	    @Override
	    public String evaluate(Random r, Map<String, Symbolable> symbolTable, Map<String, Integer> select)
	    {
		return null;
	    }
	};
    }
    
    private static List<Tuple<LazyToken, String>> getNextTokens(Reader reader) throws IOException
    {
	List<Tuple<LazyToken, String>> nextTokens = LazyToken.getNextTokens(reader);
	while (nextTokens.size() == 0)
	    nextTokens = LazyToken.getNextTokens(reader);
	return nextTokens;
    }

    private static Parser sequenceParser()
    {
	return new Parser()
	{
	    @Override
	    public Symbolable parse(Reader reader)
	    {
		//Iterable<Tuple<LazyToken, String>> choice = LazyToken.lexUntil(reader, LazyToken.BAR, LazyToken.SEMICOLON);
		return null;
	    }
	};
    }
    
    private static Symbolable constructSequenceSymbol(List<Tuple<LazyToken, String>> input)
    {
	final List<Tuple<LazyToken, String>> sequenceValues = input;
	return new Symbolable()
	{
	    private List<Tuple<LazyToken, String>> tokens = sequenceValues;

	    public int getTokenLength()
	    {
		return tokens.size();
	    }

	    @Override
	    public String getName()
	    {
		return "SEQUENCE";
	    }

	    @Override
	    public Symbolable getChoiceAt(int index)
	    {
		return null;
	    }

	    @Override
	    public LazyToken getTokenAt(int index)
	    {
		return tokens.get(index).getFirst();
	    }

	    @Override
	    public String evaluate(Random r, Map<String, Symbolable> symbolTable, Map<String, Integer> select)
	    {
		String output = "";
		for (Tuple<LazyToken, String> nextToken : sequenceValues)
		{
		    if (nextToken.getFirst() == LazyToken.NAME)
		    {
			if (symbolTable.containsKey(nextToken.getSecond()))
			    output += symbolTable.get(nextToken.getSecond()).evaluate(r, symbolTable, select);
			else
			    output += nextToken.getSecond() + '?';
		    }
		    else
		    {
			String value = nextToken.getSecond();
			output += value.substring(1, value.length() - 1);
		    }
		}
		return output;
	    }

	    @Override
	    public List<Symbolable> getChoices()
	    {
		return null;
	    }


	    @Override
	    public boolean isAlternateStart()
	    {
		return false;
	    }


	    @Override
	    public String getSelector()
	    {
		return null;
	    }
	};
    }
}
