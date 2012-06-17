package skype.voting;

import skype.voting.application.VotingSession;

public interface VotingSessionMessageInterface {

	public abstract String getVotingStatusMessage(VotingSession votingSession);

	public abstract String buildVotingMenu(VotingSession session);

	public abstract String buildVotingMenuWithoutVoters(VotingSession targetSession);

	public abstract String getUpdatedVotingMenu(VotingSession session);

}