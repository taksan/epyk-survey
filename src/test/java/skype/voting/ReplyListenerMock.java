package skype.voting;

import java.util.concurrent.atomic.AtomicReference;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;

public class ReplyListenerMock implements ReplyListener {
	public final AtomicReference<String> replyPrivate = new AtomicReference<String>("");
	public final AtomicReference<String> reply = new AtomicReference<String>("");

	public void onReply(ChatAdapterInterface chat, String replyMessage) {
		reply.set(replyMessage);
	}

	@Override
	public void onReplyPrivate(ChatAdapterInterface chatAdapterInterface, String reply) {
		replyPrivate.set(reply);
	}
}