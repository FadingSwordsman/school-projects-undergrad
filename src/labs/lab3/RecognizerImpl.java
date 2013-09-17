package labs.lab3;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class RecognizerImpl implements Recognizer {

	@Override
	public boolean parseBal(Reader reader) throws IOException {
		if(reader.ready())
		{
			reader.mark(1);
			char next = processChar(reader.read());
			if(next == '.')
				return true;
			else
			{
				reader.reset();
				 if(next == '(')
					 return parseSingle(reader);
				 if(next == '[')
					 return parseDouble(reader);
				 if(next == '{')
					 return parseTriple(reader); 
			}
		}
		return false;
	}

	@Override
	public boolean parseSingle(Reader reader) throws IOException {
		if(reader.ready())
		{
			char next = processChar(reader.read());
			//Double check our starting rule:
			if(next == '(')
				//There should only be a single BAL, then a closing paren:
				return parseMultiple(reader, 1) && processChar(reader.read()) == ')';
		}
		return false;
	}

	@Override
	public boolean parseDouble(Reader reader) throws IOException {
		if(reader.ready())
		{
			char next = processChar(reader.read());
			//Double check our starting rule:
			if(next == '[')
				//We now expect 2 BAL values in a row:
				return parseMultiple(reader, 2) && processChar(reader.read()) == ']';
		}
		return false;
	}

	@Override
	public boolean parseTriple(Reader reader) throws IOException {
		if(reader.ready())
		{
			char next = processChar(reader.read());
			//Double check our starting rule:
			if(next == '{')
				//We now expect 3 BAL values in a row:
				return parseMultiple(reader, 3) && processChar(reader.read()) == '}';
		}
		return false;
	}
	
	/**
	 * Convenience function for parsing multiple BAL tokens in a row
	 * @param reader
	 * @param times
	 * @return
	 * @throws IOException
	 */
	private boolean parseMultiple(Reader reader, int times) throws IOException
	{
		boolean valid = true;
		for(int x = 0; x < times; x++)
			valid = valid && parseBal(reader);
		return valid;
	}
	
	/**
	 * Convenience function for parsing our input
	 * @param input
	 * @return The char that input represents, or NUL
	 */
	private static char processChar(int input)
	{
		if(Character.isDefined(input))
		{
			char[] next = Character.toChars(input);
			if(next.length == 1)
				return next[0];
		}
		return Character.toChars(0)[0];
	}
	
	public static void main(String[] args) {
		String validInput1 = ".";
		String invalidInput1 = "([.])";
		String validInput2 = "((((((.))))))";
		String invalidInput2 = "[.[.].]";
		String validInput3 = "{[.(.)].(.)}";
		String invalidInput3 = "[.)";
		String[] potentialInputs = { validInput1, invalidInput1, validInput2,
		invalidInput2, validInput3, invalidInput3 };

		Recognizer recognizer = new RecognizerImpl();

		try {
			for (int i = 0; i < 6; i++) {
				if (recognizer.parseBal(new StringReader(potentialInputs[i]))) {
					// must have recognized it!
					System.out.println("Recognizer recognized "
							+ potentialInputs[i] + " as valid.");
				} else {
					// invalid, boo.
					System.out.println("Recognizer did NOT recognize "
							+ potentialInputs[i] + " as valid.");
				}
			}
		} catch (IOException e) {
			System.out.println("IO Exception. Recognizer = Dead.");
			e.printStackTrace();
		}
	}

}
