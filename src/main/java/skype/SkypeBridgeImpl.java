package skype;


import org.apache.commons.lang.UnhandledException;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

public class SkypeBridgeImpl implements SkypeBridge {

	@Override
	public ChatAdapterInterface getChatAdapter(ChatMessage chatMessage) {
		return new ChatBridge(getChatOrCry(chatMessage), getSenderFullNameOrId(chatMessage));
	}
	
	@Override
	public void sendMessage(ChatAdapterInterface chatBridge, String message) {
		Chat chat = ((ChatBridge)chatBridge).getChat();
		sendMessageOrCry(message, chat);
	}

	public static String getSenderFullNameOrId(ChatMessage chatMessage) {
		try {
			return getUserFullNameOrId(chatMessage.getSender());
		} catch (SkypeException e) {
			throw new UnhandledException(e);
		}
	}

	private Chat getChatOrCry(ChatMessage chatMessage) {
		try {
			return chatMessage.getChat();
		} catch (SkypeException e) {
			throw new UnhandledException(e);
		}
	}

	public static String getUserFullNameOrId(User sender) {
		String fullName;
		try {
			fullName = sender.getFullName();
		} catch (SkypeException e) {
			throw new UnhandledException(e);
		}
		if (fullName==null)
			return sender.getId();
		return fullName;
	}

	private void sendMessageOrCry(String message, Chat chat) {
		try {
			chat.send(message);
		} catch (SkypeException e) {
			throw new IllegalStateException(e);
		}
	}
}
