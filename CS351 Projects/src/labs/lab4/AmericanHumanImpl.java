package labs.lab4;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * An {@code AmericanHumanImpl} extends an {@code AbstractAmericanHuman} which
 * in turn implements some functionality of a {@code Human}. The rest of the
 * {@code Human} methods are specific to the instance of this class and are
 * implemented here.
 * 
 * @author Keystone
 */
public class AmericanHumanImpl extends AbstractAmericanHuman {

	private String firstName;
	private char middleInitial;
	private String lastName;
	private Date birthday;
	private int houseNumber;
	private String houseStreet;
	private String city;
	private String state;
	private int zipCode;
	private String favoriteColor;

	/**
	 * The constructor for an {@code AmericanHumanImpl}. Initialize all of the
	 * global class vars that exist for an {@code AmericanHumanImpl}.
	 * 
	 * @param firstName
	 * @param middleInitial
	 * @param lastName
	 * @param birthday
	 * @param houseNumber
	 * @param houseStreet
	 * @param city
	 * @param state
	 * @param zipCode
	 * @param favoriteColor
	 */
	public AmericanHumanImpl(String firstName, char middleInitial,
			String lastName, Date birthday, int houseNumber,
			String houseStreet, String city, String state, int zipCode,
			String favoriteColor) {
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.lastName = lastName;
		this.birthday = birthday;
		this.houseNumber = houseNumber;
		this.houseStreet = houseStreet;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
		this.favoriteColor = favoriteColor;
	}

	@Override
	public String buildHomeAddress() {
		return houseNumber + " " + houseStreet + " " + city + ", " + state
				+ " " + zipCode + ", U.S.A";
	}

	@Override
	public int calculateDistanceToCapital() {
		// Don't actually know the distance, let's just guess.
		return new Random().nextInt();
	}

	@Override
	public String buildBirthCertificate() {
		DateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY");
		dateFormat.format(birthday);

		return firstName + " " + middleInitial + ". " + lastName + " "
				+ birthday;
	}

	@Override
	public boolean guessFavoriteColor(String color) {
		if (color.equalsIgnoreCase(favoriteColor))
			return true;

		// didn't match
		return false;
	}

	@Override
	public int calculateProbableHeightFromWeight(int weight) {
		// EH. Here's a guess:
		return weight / 3;
	}

}
