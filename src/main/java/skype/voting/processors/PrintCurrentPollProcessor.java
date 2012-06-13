package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.voting.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.PrintCurrentPollRequest;

public class PrintCurrentPollProcessor extends VotingCommandProcessorAbstract {

	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof PrintCurrentPollRequest;
	}

	@Override
	public void process(ShellCommand command) {
		final VotingSession session = executor.getSessionForRequest(command);
		String reply = executor.getUpdatedVotingMenu(session);
		onReply(command, reply);
	}

}
