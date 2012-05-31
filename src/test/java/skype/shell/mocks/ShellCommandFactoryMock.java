package skype.shell.mocks;

import skype.ChatId;
import skype.lunch.UnrecognizedCommand;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandFactory;
import skype.shell.ShellCommandVisitor;

public class ShellCommandFactoryMock implements ShellCommandFactory {
	@Override
	public ShellCommand produce(final ChatId chatId, final String message) {
		if (!understands(message))
			return new UnrecognizedCommand(null, message);
		
		return new ShellCommand() {
				@Override public String getText() {
					return "Understood";
				}
				
				@Override public void acceptSentRequest(ShellCommandVisitor visitor) {
					throw new RuntimeException("NOT IMPLEMENTED");
				}
		};
	}

	private boolean understands(String message) {
		return message.startsWith("#understood_command");
	}
}