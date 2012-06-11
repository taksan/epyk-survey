package skype.shell.mocks;

import skype.ChatAdapterInterface;
import skype.shell.CommandProcessor;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandFactory;
import skype.shell.UnrecognizedCommand;

public class ShellCommandFactoryMock implements ShellCommandFactory {
	@Override
	public ShellCommand produce(final ChatAdapterInterface chat, final String message) {
		if (!understands(message))
			return new UnrecognizedCommand(chat, message);
		
		return new ShellCommand() {
				@Override public String getText() {
					return "Understood";
				}
				
				@Override public void beProcessedAsSentMessage(CommandProcessor visitor) {
					throw new RuntimeException("NOT IMPLEMENTED");
				}

				@Override
				public ChatAdapterInterface getChat() {
					throw new RuntimeException("NOT IMPLEMENTED");
				}

				@Override
				public void beProcessedAsReceivedMessage(
						CommandProcessor processor) {
					throw new RuntimeException("NOT IMPLEMENTED");
				}
		};
	}

	public boolean understands(String message) {
		return message.startsWith("#understood_command");
	}

	@Override
	public String getHelp() {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
}