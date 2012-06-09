package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandProcessor;

public class ClosePollRequest extends AbstractShellCommand {

	public ClosePollRequest(ChatAdapterInterface chat, String command) {
		super(chat, command);
	}

	@Override
	public void acceptProcessorForSentMessages(CommandProcessor processor) {
		processor.processClosePollRequest(this);
	}

	@Override
	public void acceptProcessorForReceivedMessages(CommandProcessor processor) {
		processor.processClosePollRequest(this);
	}
}
