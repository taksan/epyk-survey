package skype.shell.mocks;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandInterpreter;

public class ShellCommandFactoryMock implements ShellCommandInterpreter {
	@Override
	public ShellCommand processMessage(final ChatAdapterInterface chat, final String message) {
		if (!understands(message))
			throw new IllegalStateException("It is is a bug to get here");
		
		return new ShellCommand() {
				@Override public String getText() {
					return "Understood";
				}
				
				@Override
				public ChatAdapterInterface getChat() {
					throw new RuntimeException("NOT IMPLEMENTED");
				}
		};
	}

	public boolean understands(String message) {
		return message.startsWith("#understood_command");
	}

	@Override
	public String getHelp() {
		return "ShellCommandFactoryMock help";
	}
}