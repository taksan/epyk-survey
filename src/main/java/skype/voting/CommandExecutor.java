package skype.voting;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;

public interface CommandExecutor {
	public boolean processMessage(ChatAdapterInterface chat, String message);
	public void setReplyListener(ReplyListener listener);
}
