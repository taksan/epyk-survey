package skype.voting;

public interface VotingPollVisitor {
	public void onWelcomeMessage(String message);
	public void visitOption(VotingPollOption option);
	public void visitParticipant(String participantName);
}
