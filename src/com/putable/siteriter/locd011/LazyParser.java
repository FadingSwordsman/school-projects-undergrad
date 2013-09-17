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

public enum LazyParser {
	RULE(ruleParser()),
	HEAD(headParser()),
	CHOICE(choiceParser()),
	SEQUENCE(sequenceParser());
	
	private Parser parser;
	
	public static Iterable<Symbolable> parse(Reader reader) throws IOException
	{
		Iterable<Tuple<LazyToken, String>> nextRule = LazyToken.lexUntil(reader, LazyToken.SEMICOLON);
		while(notComplete(nextRule))
		{
			Iterator<Tuple<LazyToken, String>> ruleStream = nextRule.iterator();
			RULE.parseStream(ruleStream);
			nextRule = LazyToken.lexUntil(reader, LazyToken.SEMICOLON, LazyToken.EOI);
		}
		if(nextRule.iterator().next().getFirst() != LazyToken.EOI)
			throw new SDLParseException("Expected a SEMICOLON at the end of the rule!");
		return new LinkedList<Symbolable>();
	}
	
	private static boolean notComplete(Iterable<Tuple<LazyToken, String>> rule)
	{
		boolean complete = false;
		for(Tuple<LazyToken, String> next : rule)
			complete = complete && next.getFirst() == LazyToken.EOI;
		return !complete;
	}
	
	private LazyParser(Parser parser)
	{
		this.parser = parser;
	}

	private List<Symbolable> parseStream(Iterator<Tuple<LazyToken, String>> inputIterator)
	{
		return parser.parse(inputIterator);
	}
	
	private interface Parser
	{
		public List<Symbolable> parse(Iterator<Tuple<LazyToken, String>> input);
	}
	
	private static Parser ruleParser()
	{
		return new Parser()
		{
			public List<Symbolable> parse(Iterator<Tuple<LazyToken, String>> input)
			{
				List<Symbolable> head = HEAD.parser.parse(input);
				List<Symbolable> choices = CHOICE.parser.parse(input);
				List<Symbolable> rules = new LinkedList<Symbolable>();
				rules.add(constructRuleSymbol(head, choices));
				return rules;
			}
		};
	}
	
	private static Parser headParser()
	{
		return new Parser()
		{

			@Override
			public List<Symbolable> parse(Iterator<Tuple<LazyToken, String>> input) {
				List<Symbolable> head = new LinkedList<Symbolable>();
				List<Tuple<LazyToken, String>> headValues;
				while(input.hasNext())
				{
					Tuple<LazyToken, String> nextToken = input.next();
					if(nextToken.getFirst() == LazyToken.EQUAL)
					{
						head.add(constructHead(headValues));
						return head;
					}
					headValues.add(nextToken);
				}
				return null;
			}
		};
	}

	private static Parser choiceParser()
	{
		return new Parser()
		{
			@Override
			public Iterable<Symbolable> parse(Iterator<Tuple<LazyToken, String>> input) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}
	
	private static Parser sequenceParser()
	{
		return new Parser()
		{
			@Override
			public Iterable<Symbolable> parse(Iterator<Tuple<LazyToken, String>> input) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	private static Symbolable constructRuleSymbol(List<Symbolable> head, List<Symbolable> choices)
	{
		return new Symbolable()
		{

			@Override
			public String getName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Symbolable getChoiceAt(int index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public LazyToken getTokenAt(int index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public int getTokenLength() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public String evaluate(Random r,
					Map<String, Symbolable> symbolTable,
					Map<String, Integer> select) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
	}
	
	private static Symbolable constructHead(Iterator<Tuple<Token, String>> input)
	{
		//Grammar: [:]NAME[:NAME]
		if(!input.hasNext())
			throw new SDLParseException("Expected COLON or NAME")
	}
	
	private static Symbolable constructSequenceSymbol(Iterator<Tuple<Token,String>> input)
	{
		final List<Tuple<LazyToken, String>> sequenceValues = new ArrayList<Tuple<LazyToken, String>>();
		
		return new Symbolable()
		{
			private List<Tuple<LazyToken, String>> tokens;
			
			public int getTokenLength()
			{
				return tokens.size();
			}

			@Override
			public String getName() {
				return "SEQUENCE";
			}

			@Override
			public Symbolable getChoiceAt(int index) {
				return this;
			}

			@Override
			public LazyToken getTokenAt(int index) {
				return tokens.get(index).getFirst();
			}

			@Override
			public String evaluate(Random r, Map<String, Symbolable> symbolTable, Map<String, Integer> select) {
				String output = "";
				for(Tuple<LazyToken, String> nextToken : sequenceValues)
				{
					if(nextToken.getFirst() == LazyToken.NAME)
					{
						if(symbolTable.containsKey(nextToken.getSecond()))
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
				
		};
	}
}

