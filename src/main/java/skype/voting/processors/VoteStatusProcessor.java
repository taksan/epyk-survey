package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.voting.VotingPollCommandProcessor;
import skype.voting.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.VoteStatusRequest;

public class VoteStatusProcessor extends VotingCommandProcessorAbstract {

	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof VoteStatusRequest;
	}

	@Override
	public void process(ShellCommand command) {
		if (!manager.isInitializedSessionOnRequestChat(command)) return;
		VoteStatusRequest request = (VoteStatusRequest) command;
		
		final VotingSession votingSession = manager.getSessionForRequest(request);
		String status = "Votes: "+VotingPollCommandProcessor.getVotingStatusMessage(votingSession);
		onReplyPrivate(command, status);
	}
}
