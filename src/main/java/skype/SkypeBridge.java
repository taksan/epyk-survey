package skype;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;


public interface SkypeBridge {
	public void sendMessage(ChatAdapterInterface chatBridgeInterface, String message);

	public ChatAdapterInterface getChatAdapter(ChatMessage sentChatMessage);
	public String getContent(ChatMessage receivedChatMessage) throws SkypeException;

	String getUserFullNameOrId(User sender);

}
