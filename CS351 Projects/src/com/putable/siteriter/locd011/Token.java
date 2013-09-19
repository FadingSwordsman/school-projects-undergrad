package com.putable.siteriter.locd011;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.putable.siteriter.SDLParseException;

public enum Token {
	EQUAL(singleCharacterLexer("EQUAL", '=')),
	BAR(singleCharacterLexer("BAR", '|')),
	COLON(singleCharacterLexer("COLON", ':')),
	SEMICOLON(singleCharacterLexer("SEMICOLON", ';')),
	DLITERAL(matchedCharacterLexer("DLITERAL", '\'')),
	SLITERAL(matchedCharacterLexer("SLITERAL", '\"')),
	EOI(emptyCharacterLexer("EOI")),
	NAME(anyCharacterLexer("NAME"));
	
	private Lexer lexer;
	
	private Token(Lexer lexer)
	{
		this.lexer = lexer;
	}
	
	public static List<Tuple<Token, String>> lexUntil(Reader reader, Token... endConditions) throws IOException
	{
	    List<Tuple<Token, String>> tokens = new ArrayList<Tuple<Token, String>>();
	    List<Tuple<Token, String>> nextTokens = getNextTokens(reader);
	    
	    while(nextTokens.size() == 0 || nextTokens.get(nextTokens.size()-1).getFirst() != Token.EOI)
	    {
		tokens.addAll(nextTokens);
		for(Token endToken : endConditions)
		    for(Tuple<Token, String> tokenToCheck : nextTokens)
			if(tokenToCheck.getFirst() == endToken)
			    return tokens;
		nextTokens = getNextTokens(reader);
	    }
	    tokens.addAll(nextTokens);
	    return tokens;
	}
	
	public static List<Tuple<Token, String>> getNextTokens(Reader reader) throws IOException
	{
		char nextChar = Utility.processChar(reader.read());
		for(Token toCheck : Token.values())
			if(toCheck.canStart(nextChar))
				return toCheck.lex(reader, nextChar);
		//Empty response (Probably whitespace)
		return new ArrayList<Tuple<Token, String>>();
	}
	
	private boolean canStart(char nextChar)
	{
		return lexer.canStart(nextChar);
	}
	
	private List<Tuple<Token, String>> lex(Reader reader, char nextChar) throws IOException
	{
		return lexer.lex(reader, nextChar);
	}
	
	private static Lexer singleCharacterLexer(final String tokenName, final char value)
	{
		return new Lexer()
		{
			public List<Tuple<Token, String>> lex(Reader reader, char starting) throws SDLParseException
			{
				List<Tuple<Token, String>> tokens = new ArrayList<Tuple<Token, String>>();
				if(starting == value)
				{
					tokens.add(new Tuple<Token, String>(Token.valueOf(tokenName), Character.toString(value)));
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
	
	private static Lexer matchedCharacterLexer(final String tokenName, final char value)
	{
		return new Lexer()
		{
			@Override
			public List<Tuple<Token, String>> lex(Reader reader, char starting) throws IOException {
				int nextInt = reader.read();
				String returnValue = "" + starting;
				List<Tuple<Token, String>> literal = new LinkedList<Tuple<Token, String>>();
				while(nextInt != -1)
				{
					char nextChar = Utility.processChar(nextInt);
					returnValue += nextChar;
					if(nextChar == starting)
					{
						literal.add(new Tuple<Token, String>(Token.valueOf(tokenName), returnValue));
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
	
	private static Lexer emptyCharacterLexer(final String tokenName)
	{
		final char value = (char) -1;
		return new Lexer()
		{
			public List<Tuple<Token, String>> lex(Reader reader, char starting)
			{
				List<Tuple<Token, String>> toReturn = new LinkedList<Tuple<Token, String>>();
				toReturn.add(new Tuple<Token, String>(Token.valueOf(tokenName), Character.toString(value)));
				return toReturn;
			}
			
			public boolean canStart(char toCheck)
			{
				return toCheck == value;
			}
		};
	}
	
	private static Lexer anyCharacterLexer(final String tokenName)
	{
		return new Lexer()
		{

			@Override
			public List<Tuple<Token, String>> lex(Reader reader, char starting) throws IOException {
				char nextChar = Utility.processChar(reader.read());
				String output = Character.toString(starting);
				List<Tuple<Token, String>> values = new LinkedList<Tuple<Token, String>>();
				Token thisToken = Token.valueOf(tokenName);
				while(canStart(nextChar))
				{
					for(Token token : Token.values())
						if(token != thisToken && token.canStart(nextChar))
						{
							values.add(new Tuple<Token, String>(thisToken, output));
							values.addAll(token.lex(reader, nextChar));
							return values;
						}
					output += nextChar;
					nextChar = Utility.processChar(reader.read());
				}
				values.add(new Tuple<Token, String>(thisToken, output));
				return values;
			}

			@Override
			public boolean canStart(char toCheck) {
				return !Character.isWhitespace(toCheck);
			}
		};
	}
}
