package skype.voting.processors;

import skype.shell.ShellCommand;
import skype.shell.UnrecognizedCommand;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;

public class UnrecognizedVotingProcessor extends VotingCommandProcessorAbstract {

	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof UnrecognizedCommand;
	}

	@Override
	public void process(ShellCommand command) {
		if (!command.getText().startsWith("#"))
			return;
		
		onReplyPrivate(
				command, 
				String.format("'%s' not recognized", command.getText()));
	}

}
