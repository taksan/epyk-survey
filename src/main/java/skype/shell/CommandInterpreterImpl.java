package skype.shell;

import skype.ChatId;
import skype.lunch.UnrecognizedCommand;

public class CommandInterpreterImpl implements CommandInterpreter {
	private final ShellCommandFactory[] factories;

	public CommandInterpreterImpl(ShellCommandFactory ...factories){
		this.factories = factories;
	}

	@Override
	public ShellCommand processMessage(String chatId, String message) {
		for (ShellCommandFactory aFactory : factories) {
			ShellCommand shellCommand = aFactory.produce(new ChatId(chatId), message);
			if (shellCommand != null)
				return shellCommand;
		}
		
		return new UnrecognizedCommand(chatId, message);
	}
}
