package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandFactory;

public class ValidatedShellCommandFactory implements ShellCommandFactory {
	
	private final ShellCommandFactory factory;

	public ValidatedShellCommandFactory(ShellCommandFactory factory) {
		this.factory = factory;
	}

	@Override
	public ShellCommand produce(ChatAdapterInterface chat, String message) {
		validateMessage(message);
		return factory.produce(chat, message);
	}

	@Override
	public boolean understands(String message) {
		return factory.understands(message);
	}
	
	protected void validateMessage(String message) {
		if (!understands(message))
			throw new InvalidCommandException(message);
	}
}
