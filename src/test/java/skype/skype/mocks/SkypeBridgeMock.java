package skype.skype.mocks;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.mocks.ChatBridgeMock;

import com.skype.ChatMessage;

public final class SkypeBridgeMock implements SkypeBridge {
	public String sentMessage="";
	public String toChatId="";
	
	@Override
	public void sendMessage(ChatAdapterInterface chatId, String message) {
		this.sentMessage = message;
		this.toChatId = chatId.toString();
	}

	@Override
	public ChatAdapterInterface getChatAdapter(ChatMessage sentChatMessage) {
		return new ChatBridgeMock("mocked");
	}
}