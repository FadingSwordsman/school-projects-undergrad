package com.putable.siteriter.locd011;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.putable.siteriter.SDLParseException;

public enum LazyToken {
	EQUAL(singleCharacterLexer("EQUAL", '=')),
	BAR(singleCharacterLexer("BAR", '|')),
	COLON(singleCharacterLexer("COLON", ':')),
	SEMICOLON(singleCharacterLexer("SEMICOLON", ';')),
	DLITERAL(matchedCharacterLexer("DLITERAL", '\'')),
	SLITERAL(matchedCharacterLexer("SLITERAL", '\"')),
	EOI(emptyCharacterLexer("EOI")),
	NAME(anyCharacterLexer("NAME"));
	
	private LazyLexer lexer;
	
	private LazyToken(LazyLexer lexer)
	{
		this.lexer = lexer;
	}
	
	public static List<Tuple<LazyToken, String>> lexUntil(Reader reader, LazyToken... endConditions) throws IOException
	{
	    List<Tuple<LazyToken, String>> nextTokens = getNextTokens(reader);
	    while(nextTokens.size() == 0 || nextTokens.get(nextTokens.size()-1).getFirst() != LazyToken.EOI)
	    {
		for(LazyToken endToken : endConditions)
		    for(Tuple<LazyToken, String> tokenToCheck : nextTokens)
			if(tokenToCheck.getFirst() == endToken)
			    return nextTokens;
		nextTokens = getNextTokens(reader);
	    }
	    return nextTokens;
	}
	
	public static List<Tuple<LazyToken, String>> getNextTokens(Reader reader) throws IOException
	{
		char nextChar = Utility.processChar(reader.read());
		for(LazyToken toCheck : LazyToken.values())
			if(toCheck.canStart(nextChar))
				return toCheck.lex(reader, nextChar);
		//Empty response (Probably whitespace)
		return new ArrayList<Tuple<LazyToken, String>>();
	}
	
	private boolean canStart(char nextChar)
	{
		return lexer.canStart(nextChar);
	}
	
	private List<Tuple<LazyToken, String>> lex(Reader reader, char nextChar) throws IOException
	{
		return lexer.lex(reader, nextChar);
	}
	
	private static LazyLexer singleCharacterLexer(final String tokenName, final char value)
	{
		return new LazyLexer()
		{
			public List<Tuple<LazyToken, String>> lex(Reader reader, char starting) throws SDLParseException
			{
				List<Tuple<LazyToken, String>> tokens = new ArrayList<Tuple<LazyToken, String>>();
				if(starting == value)
				{
					tokens.add(new Tuple<LazyToken, String>(LazyToken.valueOf(tokenName), Character.toString(value)));
					return tokens;
				}
				throw new SDLParseException("Could not properly lex " + tokenName + "! Expected " + value + " but was " + starting + "!");
			}
			
			public boolean canStart(char toCheck)
			{
				return toCheck == value;
			}
		};
	}
	
	private static LazyLexer matchedCharacterLexer(final String tokenName, final char value)
	{
		return new LazyLexer()
		{
			@Override
			public List<Tuple<LazyToken, String>> lex(Reader reader, char starting) throws IOException {
				int nextInt = reader.read();
				String returnValue = "" + starting;
				List<Tuple<LazyToken, String>> literal = new LinkedList<Tuple<LazyToken, String>>();
				while(nextInt != -1)
				{
					char nextChar = Utility.processChar(nextInt);
					returnValue += nextChar;
					if(nextChar == starting)
					{
						literal.add(new Tuple<LazyToken, String>(LazyToken.valueOf(tokenName), returnValue));
						return literal;
					}
					nextInt = reader.read();
				}
				throw new SDLParseException("Could not find matching " + returnValue + "!");
			}
			
			public boolean canStart(char toCheck)
			{
				return toCheck == value;
			}
		};
	}
	
	private static LazyLexer emptyCharacterLexer(final String tokenName)
	{
		final char value = (char) -1;
		return new LazyLexer()
		{
			public List<Tuple<LazyToken, String>> lex(Reader reader, char starting)
			{
				List<Tuple<LazyToken, String>> toReturn = new LinkedList<Tuple<LazyToken, String>>();
				toReturn.add(new Tuple<LazyToken, String>(LazyToken.valueOf(tokenName), Character.toString(value)));
				return toReturn;
			}
			
			public boolean canStart(char toCheck)
			{
				return toCheck == value;
			}
		};
	}
	
	private static LazyLexer anyCharacterLexer(final String tokenName)
	{
		return new LazyLexer()
		{

			@Override
			public List<Tuple<LazyToken, String>> lex(Reader reader, char starting) throws IOException {
				char nextChar = Utility.processChar(reader.read());
				String output = Character.toString(starting);
				List<Tuple<LazyToken, String>> values = new LinkedList<Tuple<LazyToken, String>>();
				LazyToken thisToken = LazyToken.valueOf(tokenName);
				while(canStart(nextChar))
				{
					for(LazyToken token : LazyToken.values())
						if(token != thisToken && token.canStart(nextChar))
						{
							values.add(new Tuple<LazyToken, String>(thisToken, output));
							values.addAll(token.lex(reader, nextChar));
							return values;
						}
					output += nextChar;
					nextChar = Utility.processChar(reader.read());
				}
				values.add(new Tuple<LazyToken, String>(thisToken, output));
				return values;
			}

			@Override
			public boolean canStart(char toCheck) {
				return !Character.isWhitespace(toCheck);
			}
		};
	}
}
