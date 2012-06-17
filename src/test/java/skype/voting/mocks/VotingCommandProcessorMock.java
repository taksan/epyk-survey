package skype.voting.mocks;

import skype.shell.CommandInterpreter;
import skype.shell.ShellCommand;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;

public final class VotingCommandProcessorMock extends VotingCommandProcessorAbstract {
	private ShellCommand processedCommand = null;
	
	public VotingCommandProcessorMock(CommandInterpreter interpreter) {
		super(interpreter);
	}

	@Override
	public void process(ShellCommand command) {
		this.processedCommand = command;
	}

	@Override
	public boolean canProcess(ShellCommand command) {
		return true;
	}

	public ShellCommand getProcessedCommand() {
		return processedCommand;
	}
}