package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.voting.application.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.VoteStatusRequest;
import skype.voting.requests.factories.VoteStatusInterpreter;

public class VoteStatusProcessor extends VotingCommandProcessorAbstract {
	
	public VoteStatusProcessor() {
		super(new VoteStatusInterpreter());
	}

	@Override
	public void process(ShellCommand command) {
		if (!executor.isInitializedSessionOnRequestChat(command)) return;
		VoteStatusRequest request = (VoteStatusRequest) command;
		
		final VotingSession votingSession = executor.getSessionForRequest(request);
		String status = "Votes: "+messages.getVotingStatusMessage(votingSession);
		onReplyPrivate(command, status);
	}
}
