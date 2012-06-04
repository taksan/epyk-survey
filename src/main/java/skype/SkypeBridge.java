package skype;

import com.skype.ChatMessage;


public interface SkypeBridge {
	public void sendMessage(ChatAdapterInterface chatBridgeInterface, String message);

	public ChatAdapterInterface getChatAdapter(ChatMessage sentChatMessage);
}
