package skype.voting.requests;

import skype.voting.application.VotingPollOption;

public interface VotingPollVisitor {
	public void onWelcomeMessage(String message);
	public void visitOption(VotingPollOption option);
	public void visitParticipant(String participantName);
}
