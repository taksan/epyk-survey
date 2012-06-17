package skype.voting;

import skype.shell.ShellCommandProcessor;

public interface VotingCommandProcessor extends ShellCommandProcessor {
	public void setVoteSessionProvider(VotingSessionModel votingPollCommandProcessor);

	public void setVoteSessionMessages(VotingSessionMessageInterface voteSessionMessages);
}
