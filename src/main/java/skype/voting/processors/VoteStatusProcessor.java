package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.voting.application.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.processors.interpreters.VoteStatusInterpreter;
import skype.voting.processors.requests.VoteStatusRequest;

public class VoteStatusProcessor extends VotingCommandProcessorAbstract {
	
	public VoteStatusProcessor() {
		super(new VoteStatusInterpreter());
	}

	@Override
	public void process(ShellCommand command) {
		if (!votingModel.isInitializedSessionOnRequestChat(command)) return;
		VoteStatusRequest request = (VoteStatusRequest) command;
		
		final VotingSession votingSession = votingModel.getSessionForRequest(request);
		String status = "Votes: "+messages.getVotingStatusMessage(votingSession);
		onReplyPrivate(command, status);
	}
}
