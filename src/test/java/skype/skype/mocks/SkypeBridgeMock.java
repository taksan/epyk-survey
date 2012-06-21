package skype.skype.mocks;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.mocks.ChatBridgeMock;

import com.skype.ChatMessage;
import com.skype.User;

public final class SkypeBridgeMock implements SkypeBridge {
	public String sentMessage="";
	public String toChatId="";
	
	@Override
	public String sendMessage(ChatAdapterInterface chatId, String message) {
		this.sentMessage = message;
		this.toChatId = chatId.toString();
		return toChatId;
	}

	@Override
	public ChatAdapterInterface getChatAdapter(ChatMessage sentChatMessage) {
		return new ChatBridgeMock("mocked");
	}

	@Override
	public String getUserFullNameOrId(User sender) {
		return sender.getId();
	}

	@Override
	public String getContent(ChatMessage receivedChatMessage) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public String sendMessageToUser(String fullname, String message) {
		return "";
	}

	@Override
	public String getMessageId(ChatMessage receivedChatMessage) {
		return "";
	}
}