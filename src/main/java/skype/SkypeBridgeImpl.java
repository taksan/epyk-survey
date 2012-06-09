package skype;


import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.UnhandledException;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

public class SkypeBridgeImpl implements SkypeBridge {
	Map<Chat, ChatAdapterInterface> bridgeByChat = new LinkedHashMap<Chat, ChatAdapterInterface>();
	
	static SkypeBridgeImpl singleton = new SkypeBridgeImpl();
	
	private SkypeBridgeImpl(){}
	
	public static SkypeBridgeImpl get() {
		return singleton;
	}

	@Override
	public ChatAdapterInterface getChatAdapter(ChatMessage chatMessage) {
		Chat actualChat = getChatOrCry(chatMessage);
		ChatAdapterInterface chatBridge = bridgeByChat.get(actualChat);
		if (bridgeByChat.get(actualChat) == null) {
			chatBridge = new ChatBridge(actualChat);
			bridgeByChat.put(actualChat, chatBridge);
		}
		chatBridge.setLastSender(getSenderFullNameOrId(chatMessage));
		return chatBridge;
	}
	
	@Override
	public void sendMessage(ChatAdapterInterface chatBridge, String message) {
		Chat chat = ((ChatBridge)chatBridge).getChat();
		sendMessageOrCry(message, chat);
	}

	public String getSenderFullNameOrId(ChatMessage chatMessage) {
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

	@Override
	public String getUserFullNameOrId(User sender) {
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
