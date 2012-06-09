package skype;

import com.skype.ChatMessage;
import com.skype.User;


public interface SkypeBridge {
	public void sendMessage(ChatAdapterInterface chatBridgeInterface, String message);

	public ChatAdapterInterface getChatAdapter(ChatMessage sentChatMessage);

	String getUserFullNameOrId(User sender);
}
