package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandProcessor;
import skype.shell.ShellCommand;

public class ClosePollRequest extends AbstractShellCommand implements ShellCommand {

	public ClosePollRequest(ChatAdapterInterface chat, String command) {
		super(chat, command);
	}

	@Override
	public void beProcessedAsSentMessage(CommandProcessor processor) {
		processor.processClosePollRequest(this);
	}

	@Override
	public void beProcessedAsReceivedMessage(CommandProcessor processor) {
		processor.processClosePollRequest(this);
	}
}
