package com.putable.siteriter.locd011;

import java.util.List;
import java.util.Map;
import java.util.Random;

public interface Symbol {
	public String getName();
	public boolean isAlternateStart();
	public String getSelector();
	public List<Symbol> getChoices();
	public Symbol getChoiceAt(int index);
	public LazyToken getTokenAt(int index);
	public int getTokenLength();
	public String evaluate(Random r, Map<String, Symbol> symbolTable, Map<String, Integer> select);
}