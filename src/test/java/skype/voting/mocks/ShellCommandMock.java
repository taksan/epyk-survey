package skype.voting.mocks;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;

public class ShellCommandMock implements ShellCommand {

	@Override
	public String getText() {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public ChatAdapterInterface getChat() {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
}
