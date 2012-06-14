package skype.voting.application;


public class ThrowsFeedbackHandler implements VoteFeedbackHandler {

	@Override
	public void handleError(String errorMessage) {
		throw new RuntimeException(errorMessage);
	}

	@Override
	public void handleSuccess() {
	}

}
