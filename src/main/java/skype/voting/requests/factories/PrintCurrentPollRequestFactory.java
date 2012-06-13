package skype.voting.requests.factories;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandFactory;

public class PrintCurrentPollRequestFactory implements ShellCommandFactory {
	
	@Override
	public ShellCommand produce(ChatAdapterInterface chat, String message) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public boolean understands(String message) {
		return false;
	}
	
	@Override
	public String getHelp() {
		return "NOT IMPLEMENTED";
	}
}
