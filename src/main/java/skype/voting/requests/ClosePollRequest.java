package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandProcessor;

public class ClosePollRequest extends AbstractShellCommand {

	public ClosePollRequest(ChatAdapterInterface chat, String command) {
		super(chat, command);
	}

	@Override
	public void acceptProcessorForSentMessages(CommandProcessor visitor) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void acceptProcessorForReceivedMessages(CommandProcessor processor) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

}
