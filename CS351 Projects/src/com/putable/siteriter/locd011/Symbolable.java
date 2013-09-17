package com.putable.siteriter.locd011;

import java.util.Map;
import java.util.Random;

public interface Symbolable {
	public String getName();
	public Symbolable getChoiceAt(int index);
	public Token getTokenAt(int index);
	public int getTokenLength();
	public String evaluate(Random r, Map<String, Symbol> symbolTable, Map<String, Integer> select);
}