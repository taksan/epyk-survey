package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandProcessor;
import skype.shell.ShellCommand;

public class VoteStatusRequest extends AbstractShellCommand implements ShellCommand {
	public VoteStatusRequest(ChatAdapterInterface chat, String command) {
		super(chat, command);
	}

	@Override
	public void beProcessedAsSentMessage(CommandProcessor processor) {
		processor.processVoteStatusRequest(this);
	}

	@Override
	public void beProcessedAsReceivedMessage(CommandProcessor processor) {
		processor.processVoteStatusRequest(this);
	}

}
