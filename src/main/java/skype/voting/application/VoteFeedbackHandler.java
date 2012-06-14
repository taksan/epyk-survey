package skype.voting.application;

public interface VoteFeedbackHandler {
	public void handleError(String errorMessage);
	public void handleSuccess();
}
