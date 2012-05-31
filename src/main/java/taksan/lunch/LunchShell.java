package taksan.lunch;

import java.util.LinkedHashSet;
import java.util.Set;


public class LunchShell {
	Set<LunchParticipant> participants = new LinkedHashSet<LunchParticipant>();

	public void addParticipant(LunchParticipant lunchParticipant) {
		participants.add(lunchParticipant);
	}

	public void accept(ParticipantVisitor participantVisitor) {
		for (LunchParticipant participant  : participants) {
			participantVisitor.visit(participant);
		}
	}

	public void requestLunchDecision() {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

}