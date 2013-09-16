package com.putable.siteriter.locd011;

import java.io.IOException;
import java.io.Reader;

import com.putable.siteriter.SDLParseException;

/**
 * 
 * Handle token parsing in a way we can easily create, remove, or modify tokens
 * 
 * @author David
 * 
 * 
 */
public enum Token
{
    EQUAL(singleCharacterLexer('=', "EQUAL")),
    BAR(singleCharacterLexer('|', "BAR")),
    COLON(singleCharacterLexer(':', "COLON")),
    SEMICOLON(singleCharacterLexer(';', "SEMICOLON")),
    DLITERAL(matchedCharacterLexer('\"', "DLITERAL")),
    SLITERAL(matchedCharacterLexer('\'', "SLITERAL")),
    EOI,
    NAME(anyCharacterLexer("NAME", "NAME"));
    private Lexer lexer;

    // A special default constructor for the EOI character
    private Token()
    {
	this.lexer = new Lexer()
	{
	    @Override
	    public String tokenize(char character, Reader reader)
	    {
		return "";
	    }

	    @Override
	    public char getRepresentation()
	    {
		return 0;
	    }

	    @Override
	    public boolean canStart(char character)
	    {
		return character == Character.toChars(0)[0];
	    }
	};
    }

    private Token(Lexer parser)
    {
	this.lexer = parser;
    }

    /**
     * 
     * Return the token that corresponds to the specified input.
     * 
     * @param input
     * 
     * @return
     * 
     * @throws SDLParseException
     */
    public static Token getToken(char input) throws SDLParseException
    {
	for (Token next : Token.values())
	    if (next.canStart(input))
		return next;
	throw new SDLParseException("No token defined for: <" + input + ">");
    }

    public Lexer getLexer()
    {
	return lexer;
    }

    private boolean canStart(char input)
    {
	return lexer.canStart(input);
    }

    /**
     * 
     * Construct a simple lexer for any standalone token.
     * 
     * @param tokenValue
     * 
     * @param tokenName
     * 
     * @return
     */
    private static Lexer singleCharacterLexer(final char tokenValue, final String tokenName)
    {
	return new Lexer()
	{
	    public String tokenize(char character, Reader reader) throws IOException
	    {
		if (character == tokenValue)
		    return Character.toString(character);
		throw new SDLParseException("Invalid recognition of " + tokenName + " token!");
	    }

	    public char getRepresentation()
	    {
		return tokenValue;
	    }

	    public boolean canStart(char character)
	    {
		return character == tokenValue;
	    }
	};
    }

    /**
     * 
     * Construct a lexer that works with "matched" values, or values that start
     * and end with the same character
     * 
     * @param tokenValue
     * 
     * @param tokenName
     * 
     * @return
     */
    private static Lexer matchedCharacterLexer(final char tokenValue, final String tokenName)
    {
	return new Lexer()
	{
	    public String tokenize(char startValue, Reader reader) throws IOException
	    {
		String tokenValue = Character.toString(startValue);
		while (reader.ready())
		{
		    // Simple processor that takes any input, and only kicks
		    // out if the read token matches the first
		    // TODO: Enhancement (Not conforming to spec!): Add
		    // ability to 'escape' characters
		    char nextChar = Utility.processChar(reader.read());
		    tokenValue += nextChar;
		    if (nextChar == startValue)
			return tokenValue;
		    if(nextChar == Utility.undefinedChar)
			throw new SDLParseException("Did not find matching character <" + startValue + ">");
		}
		// Our lexing has failed, since we found no match, and
		// reached the end of our stream:
		throw new SDLParseException("Did not find matching character <" + startValue + ">");
	    }

	    public char getRepresentation()
	    {
		return tokenValue;
	    }

	    public boolean canStart(char character)
	    {
		return character == tokenValue;
	    }
	};
    }

    /**
     * 
     * Construct a lexer that takes any other non-token, non-whitespace
     * character and terminates on non-specified tokens
     * 
     * @param tokenName
     * 
     * @param ignorableTokens
     *            Tokens that, if found within this token, doesn't stop lexing
     *            this token
     * 
     * @return
     */
    private static Lexer anyCharacterLexer(final String tokenName, final String... ignorableTokens)
    {
	return new Lexer()
	{
	    public String tokenize(char value, Reader reader) throws IOException
	    {
		String name = "" + value;
		boolean continuing = true;
		while (continuing)
		{
		    reader.mark(1);
		    char nextChar = Utility.processChar(reader.read());
		    if (Character.isWhitespace(nextChar) || Utility.undefinedChar == nextChar)
			return name;
		    Token tokenCheck = Token.getToken(nextChar);
		    boolean continueToken = false;
		    // Ensure that we haven't ended the current Token
		    for (int x = 0; x < ignorableTokens.length && !continueToken; x++)
			continueToken = tokenCheck == Token.valueOf(ignorableTokens[x]);
		    if (!continueToken)
		    {
			reader.reset();
			return name;
		    }
		    name += nextChar;
		}
		throw new SDLParseException("Expected end of name, but none found");
	    }

	    public char getRepresentation()
	    {
		return '*';
	    }

	    public boolean canStart(char character)
	    {
		return !Character.isWhitespace(character);
	    }
	};
    }
}
