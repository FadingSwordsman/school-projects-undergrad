package labs.lab4;

/**
 * The abstract class of an American Human. This class implements some
 * functionality of a {@link Human}. More specifically, the Capital of the
 * United States of America and the language spoken are implemented.
 * 
 * @author Keystone
 */
public abstract class AbstractAmericanHuman implements Human {
	// The capital of The United States of America
	public final String CAPITAL = "Washington, D.C.";

	@Override
	public String translateWord(String wordToTranslate) {
		if (wordToTranslate.equals("Hola"))
			return "Hello.";

		// can't translate
		return "?";
	}

	@Override
	public boolean detectLanguage(String languageName) {
		if (languageName.equals("English"))
			return true;

		// this Human is American and speaks English
		return false;
	}

	@Override
	public String getCapital() {
		return CAPITAL;
	}
}
