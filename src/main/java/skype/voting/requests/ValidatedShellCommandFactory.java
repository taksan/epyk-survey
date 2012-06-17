package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandInterpreter;

public class ValidatedShellCommandFactory implements ShellCommandInterpreter {
	
	private final ShellCommandInterpreter factory;

	public ValidatedShellCommandFactory(ShellCommandInterpreter factory) {
		this.factory = factory;
	}

	@Override
	public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
		validateMessage(message);
		return factory.processMessage(chat, message);
	}

	@Override
	public boolean understands(String message) {
		return factory.understands(message);
	}
	
	@Override
	public String getHelp() {
		return factory.getHelp();
	}
	
	protected void validateMessage(String message) {
		if (!understands(message))
			throw new InvalidCommandException(message);
	}

}
