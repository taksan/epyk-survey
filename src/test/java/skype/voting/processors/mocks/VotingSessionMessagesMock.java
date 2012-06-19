package skype.voting.processors.mocks;

import skype.voting.VotingSessionMessageInterface;
import skype.voting.application.VotingSession;

public class VotingSessionMessagesMock implements VotingSessionMessageInterface {

	@Override
	public String getVotingStatusMessage(VotingSession votingSession) {
		return "<getVotingStatusMessage>";
	}

	@Override
	public String buildVotingMenu(VotingSession session) {
		return "<buildVotingMenu>";
	}

	@Override
	public String buildVotingMenuWithoutVoters(VotingSession targetSession) {
		return "<buildVotingMenuWithoutVoters>";
	}

	@Override
	public String getUpdatedVotingMenu(VotingSession session) {
		return "<getUpdatedVotingMenu>";
	}

}
