package skype.shell;

import skype.ChatAdapterInterface;

public class CommandInterpreterImpl implements CommandInterpreter {
	private final ShellCommandFactory[] factories;

	public CommandInterpreterImpl(ShellCommandFactory ...factories){
		this.factories = factories;
	}

	@Override
	public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
		for (ShellCommandFactory aFactory : factories) {
			ShellCommand shellCommand = aFactory.produce(chat, message);
			if (shellCommand != null)
				return shellCommand;
		}
		
		return new UnrecognizedCommand(chat, message);
	}
}
