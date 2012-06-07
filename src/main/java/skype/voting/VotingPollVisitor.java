package skype.voting;

public interface VotingPollVisitor {
	public void visitOption(VotingPollOption option);

	public void visitParticipant(String participantName);
}
