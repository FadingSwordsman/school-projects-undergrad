package com.putable.siteriter.locd011;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface LazyLexer {
	public List<Tuple<LazyToken, String>> lex(Reader reader, char starting) throws IOException;
	public boolean canStart(char toCheck);
}
