package com.putable.siteriter.locd011;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface Lexer {
	public List<Tuple<Token, String>> lex(Reader reader, char starting) throws IOException;
	public boolean canStart(char toCheck);
}
