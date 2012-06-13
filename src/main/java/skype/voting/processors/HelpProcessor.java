package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.HelpRequest;

public class HelpProcessor extends VotingCommandProcessorAbstract {

	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof HelpRequest;
	}

	@Override
	public void process(ShellCommand command) {
		if (!manager.isInitializedSessionOnRequestChat(command)) return;
		HelpRequest request = (HelpRequest) command;
		
		String helpMessage = request.getHelpMessage()+"\n";
		onReplyPrivate(command, helpMessage);
	}

}
