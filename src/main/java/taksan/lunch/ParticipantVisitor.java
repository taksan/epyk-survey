package taksan.lunch;

public class ParticipantVisitor {

	StringBuffer participants=new  StringBuffer();
	public String getListOfParticipants() {
		return participants.toString().trim();
	}

	public void visit(LunchParticipant participant) {
		participants.append(participant);
		participants.append("\n");
	}

}
