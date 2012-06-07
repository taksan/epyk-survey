package skype.voting.mocks;

import skype.voting.VoteRequest;
import skype.voting.VotingPollRequest;
import skype.voting.VotingSession;

public abstract class VotingSessionMockAdapter implements VotingSession {
	public VotingPollRequest pollRequest;
	public boolean initWithInvoked = false;

	@Override
	public void initWith(VotingPollRequest request) {
		pollRequest = request;
	}

	@Override
	public void vote(VoteRequest voteRequest) {
		//
	}

}
