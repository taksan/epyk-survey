package skype.voting.processors.mocks;

import skype.voting.VotingSessionMessageInterface;
import skype.voting.application.VotingSession;

public class VotingSessionMessageMock implements VotingSessionMessageInterface {

	@Override
	public String getVotingStatusMessage(VotingSession votingSession) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public String buildVotingMenu(VotingSession session) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public String buildVotingMenuWithoutVoters(VotingSession targetSession) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public String getUpdatedVotingMenu(VotingSession session) {
		return session.toString();
	}

}
