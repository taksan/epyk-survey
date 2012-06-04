package skype.shell;

import skype.ChatAdapterInterface;

public interface ReplyListener {
	void onReply(ChatAdapterInterface chatAdapterInterface, String reply);
}
