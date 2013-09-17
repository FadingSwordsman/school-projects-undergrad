package com.putable.siteriter.locd011;

import java.io.Reader;
import java.util.LinkedList;

public enum LazyParser {
	RULE,
	HEAD,
	CHOICE,
	SEQUENCE;
	
	public Iterable<Symbolable> parse(Reader reader)
	{
		//Token.lexUntil(reader, Token.SEMICOLON);
		return new LinkedList<Symbolable>();
	}
	
	private LazyParser()
	{
		
	}
}
