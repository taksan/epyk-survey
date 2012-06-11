package skype.voting;

public interface VoteFeedbackHandler {
	public void handleError(String errorMessage);
	public void handleSuccess();
}
