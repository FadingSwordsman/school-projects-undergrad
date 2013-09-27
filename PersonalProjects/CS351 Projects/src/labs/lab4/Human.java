package labs.lab4;

/**
 * The interface of a {@link Human}. A {@code Human} can be implemented for use
 * with many different applications. Mainly, a {@code Human} consists of 3
 * different parts: the language it speaks/the location it resides,
 * characteristics about its physical nature, and information about its favorite
 * things. What a massive amount of information! Implement a {@code Human} and
 * the possibilities are ENDLESS!<br>
 * <br>
 * (Disclaimer: the possibilities are not endless; they're actually very
 * minimal.)
 * 
 * @author Keystone
 */
public interface Human {

	/**
	 * Translate the passed in {@code String} to the {@code Human}'s native
	 * language.
	 * 
	 * @param wordToTranslate
	 *            the {@code String} to translate
	 * @return the {@code String} word in the language of the {@code Human}
	 */
	String translateWord(String wordToTranslate);

	/**
	 * Check to see if the language of this {@code Human} matches the name of
	 * the language passed in.
	 * 
	 * @param languageName
	 *            the {@code String} name of the potential language
	 * @return {@code true} if the {@code languageName} matches the language of
	 *         this {@code Human}, {@code false} otherwise
	 */
	boolean detectLanguage(String languageName);

	/**
	 * Based on the house number, street, city, state and zip code of the
	 * {@code Human} , build a {@code String} that encapsulates all of the
	 * information about where this {@code Human} resides.
	 * 
	 * @return the {@code String} that is the address
	 */
	String buildHomeAddress();

	/**
	 * Calculate the distance from the home address to the country's capital.
	 * 
	 * @return the {@code int} value, rounded to the nearest mile
	 */
	int calculateDistanceToCapital();

	/**
	 * Fetch the capital of the {@code Human}'s residing country.
	 * 
	 * @return the {@code String} that is the name of the capital
	 */
	String getCapital();

	/**
	 * Build the entire information found on a birth certificate of a
	 * {@code Human}. The {@code String} value returned will be in the following
	 * form: FIRST_NAME MIDDLE_INITIAL LAST_NAME MM/DD/YYYY
	 * 
	 * @return the {@code String} in the format described above
	 */
	String buildBirthCertificate();

	/**
	 * Check to see if this {@code Human}'s favorite color matches the
	 * {@code String} parameter.
	 * 
	 * @param color
	 *            the {@code String} to check for match
	 * @return {@code true} if the passed in color is the favorite color,
	 *         {@code false} otherwise
	 */
	boolean guessFavoriteColor(String color);

	/**
	 * Based on the {@code int} weight, calculate the best guess for the height,
	 * in inches.
	 * 
	 * @param weight
	 *            the {@code int} weight value
	 * @return the {@code int} value that is the calculated guesstimation of
	 *         height in inches
	 */
	int calculateProbableHeightFromWeight(int weight);
}
