package skype;


import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.UnhandledException;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class SkypeBridgeImpl implements SkypeBridge {
	private static final String ECHO_FEEDBACK = "echo123";
	Map<Chat, ChatAdapterInterface> bridgeByChat = new LinkedHashMap<Chat, ChatAdapterInterface>();
	Map<String,String> skypeIdByFullName = new LinkedHashMap<String,String>();
	
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

	@Override
	public String getUserFullNameOrId(User sender) {
		String userId = sender.getId();
		String fullName;
		try {
			fullName = sender.getFullName();
		} catch (SkypeException e) {
			throw new UnhandledException(e);
		}
		if (fullName==null)
			fullName = userId;
		skypeIdByFullName.put(fullName, userId);
		return fullName;
	}

	private void sendMessageOrCry(String message, Chat chat) {
		try {
			chat.send(message);
		} catch (SkypeException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String getContent(ChatMessage chatMessage) throws SkypeException {
		return chatMessage.getContent();
	}

	@Override
	public void sendMessageToUser(String fullName, String message) {
		try {
			String userid = getUserIdByFullName(fullName);
			User.getInstance(userid).chat().send(message);
		} catch (SkypeException e) {
			throw new IllegalStateException(e);
		}
	}

	private String getUserIdByFullName(String fullName) throws SkypeException {
		final String userid = skypeIdByFullName.get(fullName);
		if (Skype.getProfile().getId().equals(userid)) {
			return ECHO_FEEDBACK;
		}
		return userid;
	}
	

	private Chat getChatOrCry(ChatMessage chatMessage) {
		try {
			return chatMessage.getChat();
		} catch (SkypeException e) {
			throw new UnhandledException(e);
		}
	}
}
