package skype.voting;

public class VotingPollOption {

	private final String placeName;

	public VotingPollOption(String aPlace) {
		this.placeName = aPlace;
	}

	public String getName() {
		return placeName;
	}

	public String toString() {
		return placeName;
	}
}
