package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.voting.VotingCommandProcessor;
import skype.voting.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.StartPollRequest;

public class StartPollProcessor extends VotingCommandProcessorAbstract implements VotingCommandProcessor {

	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof StartPollRequest;
	}

	@Override
	public void process(ShellCommand command) {
		VotingSession session = manager.makeNewVotingSession((StartPollRequest) command);
		
		String reply = manager.getUpdatedVotingMenu(command.getChat(), session);
		onReply(command, reply);
	}
}
