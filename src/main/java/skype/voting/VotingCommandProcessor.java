package skype.voting;

public interface VotingCommandProcessor extends ShellCommandProcessor {
	public void setVoteSessionProvider(VotingPollCommandExecutor votingPollCommandProcessor);
}
