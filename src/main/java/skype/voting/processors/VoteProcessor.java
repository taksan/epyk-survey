package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.voting.application.VoteFeedbackHandler;
import skype.voting.application.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.processors.interpreters.VoteInterpreter;
import skype.voting.processors.requests.VoteRequest;

public class VoteProcessor extends VotingCommandProcessorAbstract {
	
	public VoteProcessor() {
		super(new VoteInterpreter());
	}

	@Override
	public void process(final ShellCommand command) {
		if (!executor.isInitializedSessionOnRequestChat(command)) return;
		
		final VoteRequest request = (VoteRequest) command;
		
		final VotingSession votingSession = executor.getSessionForRequest(request);
		votingSession.vote(request, new VoteFeedbackHandler() {
			@Override
			public void handleError(String errorMessage) {
				String reply = errorMessage + ". Valid options:"+
						messages.buildVotingMenu(votingSession);

				onReply(command, reply);
			}

			@Override
			public void handleSuccess() {
				String voteStatus = messages.getVotingStatusMessage(votingSession);
				if (voteStatus.isEmpty())
					return;
				
				String reply = "Votes: " + voteStatus;
				onReply(command, reply);
			}
		});
	}
}
