package skype.lunch;

public class LunchOption {

	private final String placeName;

	public LunchOption(String aPlace) {
		this.placeName = aPlace;
	}

	public String getName() {
		return placeName;
	}

	public String toString() {
		return placeName;
	}
}
