package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandProcessor;

public class MissingVotersRequest extends AbstractShellCommand {

	public MissingVotersRequest(ChatAdapterInterface chat, String command) {
		super(chat, command);
	}

	@Override
	public void beProcessedAsSentMessage(CommandProcessor processor) {
		processor.processMissingVoteRequest(this);
	}

	@Override
	public void beProcessedAsReceivedMessage(CommandProcessor processor) {
		processor.processMissingVoteRequest(this);
	}

	
}