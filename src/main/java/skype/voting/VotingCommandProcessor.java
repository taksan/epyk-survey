package skype.voting;

import skype.shell.ShellCommandProcessor;

public interface VotingCommandProcessor extends ShellCommandProcessor {
	public void setVoteSessionProvider(VotingPollCommandExecutor votingPollCommandProcessor);
}
