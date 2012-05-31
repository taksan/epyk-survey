package taksan.skype;

import java.util.LinkedHashSet;
import java.util.Set;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

public class SkypeMessageProviderImpl implements SkypeMessageProvider, ChatMessageListener {
	Set<SkypeMessageListener> listeners = new LinkedHashSet<SkypeMessageListener>();
	public void addMessageListener(SkypeMessageListener listener) {
		listeners.add(listener);
	}

	public void chatMessageReceived(ChatMessage receivedChatMessage)
			throws SkypeException {
		String message = getMessageOrEmptyIfNull(receivedChatMessage);
		for (SkypeMessageListener listener : listeners) {
			listener.onReceivedMessage(message);
		}
	}

	public void chatMessageSent(ChatMessage sentChatMessage)
			throws SkypeException {
		String message = getMessageOrEmptyIfNull(sentChatMessage);
		for (SkypeMessageListener listener : listeners) {
			listener.onSentMessage(message);
		}
	}
	
	private String getMessageOrEmptyIfNull(ChatMessage receivedChatMessage)
			throws SkypeException {
		String message = "";
		if (receivedChatMessage != null)
			message = receivedChatMessage.getContent();
		return message;
	}

	public void messageSent(String message) {
	}
}
