package skype.voting;

import skype.alias.HelpProvider;
import skype.shell.ShellCommandProcessor;

public interface VotingCommandProcessor extends ShellCommandProcessor, HelpProvider {
	public void setVoteSessionProvider(VotingSessionModel votingPollCommandProcessor);

	public void setVoteSessionMessages(VotingSessionMessageInterface voteSessionMessages);
}
