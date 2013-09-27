package labs.lab3;

import java.io.IOException;
import java.io.Reader;

/**
 * The interface of a {@link Recognizer}. The purpose of this recognizer is to
 * decide whether a potential {@code String} input is allowed by the specified
 * grammar. The grammar that this {@link Recognizer} recognizes is the
 * following:<br>
 * <br>
 * Bal < Single | Double | Triple | '.' <br>
 * Single < '(' + Bal + ')'<br>
 * Double < '[' + Bal + Bal + ']'<br>
 * Triple < '{' + Bal + Bal + Bal + '}'<br>
 * <br>
 * where "Bal" is the start symbol. The '|' character means "either or" and the
 * '+' character means in "sequence".<br>
 * <br>
 * The {@code String input} will be given as a hardcoded {@code StringReader} in
 * the main method.
 * 
 * @author BKey
 * 
 */
public interface Recognizer {

	/**
	 * Parses the {@code Bal} non-terminal symbol. This is also the start symbol
	 * and should be the first method called when the {@link Recognizer} is run.
	 * 
	 * @param reader
	 *            the {@code Reader} which will contain the input to be read,
	 *            character-by-character, using the {@code Reader.read()}
	 *            method.
	 * @return returns {@code true} if the input in the {@link Reader} is
	 *         balanced according to the grammar, {@code false} otherwise.
	 */
	boolean parseBal(Reader reader) throws IOException;

	/**
	 * Parses the {@code Single} non-terminal symbol.
	 * 
	 * @param reader
	 *            the {@code Reader} which will contain the input to be read,
	 *            character-by-character, using the {@code Reader.read()}
	 *            method.
	 * @return returns {@code true} if the {@code Reader} reads a single '('
	 *         followed by a {@code Bal} followed by a closing ')'. Returns
	 *         {@code false} otherwise.
	 */
	boolean parseSingle(Reader reader) throws IOException;

	/**
	 * Parses the {@code Double} non-terminal symbol.
	 * 
	 * @param reader
	 *            the {@code Reader} which will contain the input to be read,
	 *            character-by-character, using the {@code Reader.read()}
	 *            method.
	 * @return returns {@code true} if the {@code Reader} reads a single '['
	 *         followed by a {@code Bal} followed by a {@code Bal} followed by a
	 *         closing ']'. Returns {@code false} otherwise.
	 */
	boolean parseDouble(Reader reader) throws IOException;

	/**
	 * Parses the {@code Triple} non-terminal symbol.
	 * 
	 * @param reader
	 *            the {@code Reader} which will contain the input to be read,
	 *            character-by-character, using the {@code Reader.read()}
	 *            method.
	 * @return returns {@code true} if the {@code Reader} reads a single '{'
	 *         followed by a {@code Bal} followed by a {@code Bal} followed by a
	 *         {@code Bal} followed by a closing '}'. Returns {@code false}
	 *         otherwise.
	 */
	boolean parseTriple(Reader reader) throws IOException;
}
