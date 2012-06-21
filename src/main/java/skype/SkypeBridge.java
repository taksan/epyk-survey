package skype;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;


public interface SkypeBridge {
	public String sendMessage(ChatAdapterInterface chatBridgeInterface, String message);
	public String sendMessageToUser(String fullName, String message);

	public ChatAdapterInterface getChatAdapter(ChatMessage sentChatMessage);
	public String getContent(ChatMessage receivedChatMessage) throws SkypeException;

	public String getUserFullNameOrId(User sender);
	public String getMessageId(ChatMessage receivedChatMessage);

}
