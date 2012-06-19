package skype.voting;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;

public class UnrecognizedRequestExecutor implements CommandExecutor {

	private ReplyListener listener;

	@Override
	public boolean processMessage(ChatAdapterInterface chat, String messageRaw) {
		String message = messageRaw.trim();
		if (!message.startsWith("#"))
			return false;
		String reply = String.format("Unrecognized command '%s'", message) ;
		listener.onReplyPrivate(chat, reply );
		return true;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		this.listener = listener;
	}

}
