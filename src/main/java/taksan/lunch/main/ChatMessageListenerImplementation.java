package taksan.lunch.main;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

public final class ChatMessageListenerImplementation implements
		ChatMessageListener {
	public void chatMessageSent(ChatMessage sentChatMessage)
			throws SkypeException {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	public void chatMessageReceived(ChatMessage receivedChatMessage)
			throws SkypeException {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
}