package com.putable.siteriter.locd011;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
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

	public static Iterable<Symbolable> parse(Reader reader) throws IOException {
		Symbolable ruleSymbol = RULE.parser.parse(reader);
		List<Symbolable> rules = new LinkedList<Symbolable>();
		rules.add(ruleSymbol);
		return new LinkedList<Symbolable>();
	}

	private static boolean notComplete(Iterable<Tuple<LazyToken, String>> rule) {
		boolean complete = false;
		for (Tuple<LazyToken, String> next : rule)
			complete = complete || next.getFirst() == LazyToken.EOI;
		return !complete;
	}

	private LazyParser(Parser parser) {
		this.parser = parser;
	}

	private interface Parser {
		public Symbolable parse(Reader reader) throws IOException;
	}

	private static Parser ruleParser() {
		return new Parser() {
			public Symbolable parse(Reader reader) throws IOException
			{
				Symbolable head = HEAD.parser.parse(reader);
				Symbolable choices = CHOICE.parser.parse(reader);
				return constructRuleSymbol(head, choices);
			}
		};
	}

	private static Parser headParser() {
		return new Parser() {
			@Override
			public Symbolable parse(Reader reader) throws IOException
			{
				return constructHead(reader);
			}
		};
	}

	private static Parser choiceParser() {
		return new Parser() {
			@Override
			public Symbolable parse(Reader reader) throws IOException
			{
				return constructChoice(reader);
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
		// Aggregate the tokens, and add the ones we removed as a byproduct of
		// parsing:
		for (int i = 0; i < head.getTokenLength(); i++)
			tokenList.add(head.getTokenAt(i));
		tokenList.add(LazyToken.EQUAL);
		for (int i = 0; i < choices.getTokenLength(); i++)
			tokenList.add(choices.getTokenAt(i));
		tokenList.add(LazyToken.SEMICOLON);

		return new Symbolable() {
			private List<Symbolable> choices = choiceList;
			private List<LazyToken> tokens = tokenList;

			@Override
			public String getName() {
				return name;
			}

			@Override
			public boolean isAlternateStart() {
				return isAlternateStart;
			}

			@Override
			public String getSelector() {
				return selector;
			}

			@Override
			public Symbolable getChoiceAt(int index) {
				return choices.get(index);
			}

			@Override
			public LazyToken getTokenAt(int index) {
				return tokens.get(index);
			}

			@Override
			public int getTokenLength() {
				return tokenList.size();
			}

			@Override
			public String evaluate(Random r, Map<String, Symbolable> symbolTable, Map<String, Integer> select) {
				if(getSelector() != null)
				{
					if(select.containsKey(getSelector()))
						if(select.get(getSelector()) != null)
							return getChoiceAt(select.get(getSelector()) % choices.size()).evaluate(r, symbolTable, select);
					Integer nextInt = r.nextInt(Integer.MAX_VALUE);
					select.put(getSelector(), nextInt);
					return getChoiceAt(nextInt % choices.size()).evaluate(r, symbolTable, select);
				}
				return choices.get(r.nextInt(choices.size())).evaluate(r, symbolTable, select);
			}

			@Override
			public List<Symbolable> getChoices() {
				return choices;
			}
		};
	}

	private static Symbolable constructHead(Reader reader) throws IOException {
		// Grammar: [:]NAME[:NAME]
		List<Tuple<LazyToken, String>> tokens = new ArrayList<Tuple<LazyToken, String>>();
		List<Tuple<LazyToken, String>> nextTokens = getNextTokens(reader);
		tokens.addAll(nextTokens);

		int currentToken = 0;
		boolean alternateStartString = nextTokens.get(0).getFirst() == LazyToken.COLON;
		if (alternateStartString) {
			currentToken++;
			if (currentToken == tokens.size()) {
				nextTokens = getNextTokens(reader);
				tokens.addAll(nextTokens);
				currentToken = 0;
			}
		}

		String symbolName;
		if (nextTokens.get(currentToken).getFirst() == LazyToken.NAME) {
			symbolName = nextTokens.get(currentToken).getSecond();
			currentToken++;
		} else
			throw new SDLParseException("Expected NAME!");

		if (currentToken == nextTokens.size()) {
			nextTokens = getNextTokens(reader);
			tokens.addAll(nextTokens);
			currentToken = 0;
		}

		String selector = null;
		if (nextTokens.get(currentToken).getFirst() == LazyToken.COLON) {
			currentToken++;
			if (currentToken == nextTokens.size()) {
				nextTokens = getNextTokens(reader);
				tokens.addAll(nextTokens);
				currentToken = 0;
			}
			if (nextTokens.get(currentToken).getFirst() == LazyToken.NAME)
			{
				selector = nextTokens.get(currentToken).getSecond();
				currentToken++;
			}
			else
				throw new SDLParseException("Expected matching NAME for associated COLON!");
		}

		if(currentToken == nextTokens.size())
		{
			nextTokens = getNextTokens(reader);
			tokens.addAll(nextTokens);
			currentToken = 0;
		}
		
		if(nextTokens.get(currentToken).getFirst() == LazyToken.EQUAL)
		{
			tokens.addAll(nextTokens);
			return constructHeadSymbol(symbolName, alternateStartString, selector, tokens);
		}
		
		throw new SDLParseException("Expected EQUAL token, but received " + nextTokens.get(currentToken).getFirst());
	}

	private static Symbolable constructHeadSymbol(final String name, final boolean alternateStartString, final String selector, final List<Tuple<LazyToken, String>> tokenRepresentation) {
		return new Symbolable() {
			private List<Tuple<LazyToken, String>> tokens = tokenRepresentation;

			@Override
			public String getName() {
				return name;
			}

			@Override
			public boolean isAlternateStart() {
				return alternateStartString;
			}

			@Override
			public String getSelector() {
				return selector;
			}

			@Override
			public List<Symbolable> getChoices() {
				return null;
			}

			@Override
			public Symbolable getChoiceAt(int index) {
				return null;
			}

			@Override
			public LazyToken getTokenAt(int index) {
				return tokens.get(index).getFirst();
			}

			@Override
			public int getTokenLength() {
				return tokens.size();
			}

			@Override
			public String evaluate(Random r,
					Map<String, Symbolable> symbolTable,
					Map<String, Integer> select) {
				return null;
			}
		};
	}

	private static Symbolable constructChoice(Reader reader) throws IOException
	{
		List<Tuple<LazyToken, String>> nextChoiceTokens = LazyToken.lexUntil(reader, LazyToken.BAR, LazyToken.SEMICOLON);
		List<LazyToken> tokenList = new ArrayList<LazyToken>();
		List<Symbolable> choices = new ArrayList<Symbolable>();
		while(notComplete(nextChoiceTokens))
		{
			for(Tuple<LazyToken, String> tokenStringPair : nextChoiceTokens)
				tokenList.add(tokenStringPair.getFirst());
			
			if(nextChoiceTokens.size() == 1)
			{
				choices.add(emptySymbol());
				LazyToken lastToken = nextChoiceTokens.get(0).getFirst();
				if(lastToken == LazyToken.SEMICOLON)
					return constructChoiceSymbolable(choices, tokenList);
			}
			
			else
			{
				LazyToken lastToken = nextChoiceTokens.get(nextChoiceTokens.size() - 1).getFirst();
				if(lastToken == LazyToken.BAR)
					choices.add(constructSequenceSymbol(nextChoiceTokens.subList(0, nextChoiceTokens.size() - 1)));
				else if(lastToken == LazyToken.SEMICOLON)
				{
					choices.add(constructSequenceSymbol(nextChoiceTokens.subList(0, nextChoiceTokens.size() - 1)));
					return constructChoiceSymbolable(choices, tokenList);
				}
			}
			nextChoiceTokens = LazyToken.lexUntil(reader, LazyToken.BAR, LazyToken.SEMICOLON);
		}
		throw new SDLParseException("SEMICOLON expected, but found EOI!");
	}
	
	private static Symbolable constructChoiceSymbolable(final List<Symbolable> choices, final List<LazyToken> tokens)
	{
		return new Symbolable()
		{

			@Override
			public String getName()
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

			@Override
			public List<Symbolable> getChoices()
			{
				return choices;
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
				return tokens.size();
			}

			@Override
			public String evaluate(Random r, Map<String, Symbolable> symbolTable, Map<String, Integer> select)
			{
				return null;
			}
		};
	}
	
	private static List<Tuple<LazyToken, String>> getNextTokens(Reader reader)
			throws IOException {
		List<Tuple<LazyToken, String>> nextTokens = LazyToken.getNextTokens(reader);
		while (nextTokens.size() == 0)
			nextTokens = LazyToken.getNextTokens(reader);
		return nextTokens;
	}

	private static Parser sequenceParser() {
		return new Parser() {
			@Override
			public Symbolable parse(Reader reader) {
				// Iterable<Tuple<LazyToken, String>> choice =
				// LazyToken.lexUntil(reader, LazyToken.BAR,
				// LazyToken.SEMICOLON);
				return null;
			}
		};
	}

	private static Symbolable constructSequenceSymbol(List<Tuple<LazyToken, String>> input) throws IOException
	{
		for(Tuple<LazyToken, String> next : input)
		{
			LazyToken type = next.getFirst();
			boolean isValid = type == LazyToken.DLITERAL || type == LazyToken.SLITERAL
					|| type == LazyToken.NAME;
			if(!isValid)
				throw new SDLParseException("Expected DLITERAL, SLITERAL, or NAME. Received " + type);
		}
		
		final List<Tuple<LazyToken, String>> sequenceValues = input;
		return new Symbolable() {
			private List<Tuple<LazyToken, String>> tokens = sequenceValues;

			public int getTokenLength() {
				return tokens.size();
			}

			@Override
			public String getName() {
				return "SEQUENCE";
			}

			@Override
			public Symbolable getChoiceAt(int index) {
				return null;
			}

			@Override
			public LazyToken getTokenAt(int index) {
				return tokens.get(index).getFirst();
			}

			@Override
			public String evaluate(Random r, Map<String, Symbolable> symbolTable, Map<String, Integer> select) {
				String output = "";
				for (Tuple<LazyToken, String> nextToken : sequenceValues) {
					if (nextToken.getFirst() == LazyToken.NAME) {
						if (symbolTable.containsKey(nextToken.getSecond()))
							output += symbolTable.get(nextToken.getSecond())
									.evaluate(r, symbolTable, select);
						else
							output += nextToken.getSecond() + '?';
					} else {
						String value = nextToken.getSecond();
						output += value.substring(1, value.length() - 1);
					}
				}
				return output;
			}

			@Override
			public List<Symbolable> getChoices() {
				return null;
			}

			@Override
			public boolean isAlternateStart() {
				return false;
			}

			@Override
			public String getSelector() {
				return null;
			}
		};
	}
	
	private static Symbolable emptySymbol()
	{
		return new Symbolable()
		{

			@Override
			public String getName()
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
				return null;
			}

			@Override
			public int getTokenLength()
			{
				return 0;
			}

			@Override
			public String evaluate(Random r, Map<String, Symbolable> symbolTable, Map<String, Integer> select)
			{
				return "";
			}
			
		};
	}
}
