package skype.lunch;

public interface LunchRequestVisitor {
	public void visitOption(LunchOption option);

	public void visitParticipant(String participantName);
}
