package com.putable.siteriter.locd011;

import java.util.Map;
import java.util.Random;

public interface Symbolable {
	public String getName();
	public Symbolable getChoiceAt(int index);
	public LazyToken getTokenAt(int index);
	public int getTokenLength();
	public String evaluate(Random r, Map<String, Symbolable> symbolTable, Map<String, Integer> select);
}