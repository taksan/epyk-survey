package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.voting.application.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.processors.interpreters.PrintCurrentPollInterpreter;

public class PrintCurrentPollProcessor extends VotingCommandProcessorAbstract {
	
	public PrintCurrentPollProcessor() {
		super(new PrintCurrentPollInterpreter());
	}

	@Override
	public void process(ShellCommand command) {
		final VotingSession session = votingModel.getSessionForRequest(command);
		String reply = messages.getUpdatedVotingMenu(session);
		onReply(command, reply);
	}

}
