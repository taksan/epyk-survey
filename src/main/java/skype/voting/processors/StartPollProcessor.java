package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.voting.VotingCommandProcessor;
import skype.voting.application.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.processors.interpreters.StartPollInterpreter;
import skype.voting.processors.requests.StartPollRequest;

public class StartPollProcessor extends VotingCommandProcessorAbstract implements VotingCommandProcessor {
	
	public StartPollProcessor() {
		super(new StartPollInterpreter());
	}

	@Override
	public void process(ShellCommand command) {
		VotingSession session = executor.makeNewVotingSession((StartPollRequest) command);
		
		String reply = messages.getUpdatedVotingMenu(session);
		onReply(command, reply);
	}
}
