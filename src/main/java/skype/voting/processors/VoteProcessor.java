package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.voting.VoteFeedbackHandler;
import skype.voting.VotingPollCommandExecutor;
import skype.voting.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.VoteRequest;

public class VoteProcessor extends VotingCommandProcessorAbstract {

	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof VoteRequest;
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
						VotingPollCommandExecutor.buildVotingMenu(votingSession);

				onReply(command, reply);
			}

			@Override
			public void handleSuccess() {
				String voteStatus = VotingPollCommandExecutor.getVotingStatusMessage(votingSession);
				if (voteStatus.isEmpty())
					return;
				
				String reply = "Votes: " + voteStatus;
				onReply(command, reply);
			}
		});
	}
}
