package taksan.lunch;

public class LunchParticipant {

	private final String participantSkypeId;

	public LunchParticipant(String participantSkypeId) {
		this.participantSkypeId = participantSkypeId;
	}

	public String toString() {
		return participantSkypeId;
	}
}
