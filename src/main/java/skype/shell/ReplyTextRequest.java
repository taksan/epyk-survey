package skype.shell;

import skype.ChatAdapterInterface;

public class ReplyTextRequest extends AbstractShellCommand {

	private final String reply;

	public ReplyTextRequest(ChatAdapterInterface chat, String command, String reply) {
		super(chat, command);
		this.reply = reply;
	}

	@Override
	public void beProcessedAsSentMessage(CommandProcessor processor) {
		processor.processReplyTextRequest(this);
	}

	@Override
	public void beProcessedAsReceivedMessage(CommandProcessor processor) {
		processor.processReplyTextRequest(this);
	}

	public String getReplyText() {
		return reply;
	}

}