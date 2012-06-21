package skype.voting;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.mocks.ChatBridgeMock;

import com.skype.ChatMessage;
import com.skype.User;

final class SkypeBridgeMock implements SkypeBridge {
	private final StringBuilder operations;
	private String nextMessageId="";

	SkypeBridgeMock(StringBuilder operations) {
		this.operations = operations;
		
	}
	@Override
	public String sendMessage(ChatAdapterInterface chatAdapter, String message) {
		operations.append("SkypeBridge: sendMessage: " + message + "\n");
		String previousId = nextMessageId;
		nextMessageId = "";
		return previousId;
	}

	@Override
	public String getUserFullNameOrId(User sender) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public ChatAdapterInterface getChatAdapter(ChatMessage sentChatMessage) {
		operations.append("getChatAdapter\n");
		return new ChatBridgeMock(null);
	}

	@Override
	public String getContent(ChatMessage receivedChatMessage) {
		operations.append("getContent\n");
		return "";
	}

	@Override
	public String sendMessageToUser(String user, String message) {
		operations.append("sendMessageToUser: " +user +" message:" + message + "\n");
		return nextMessageId;
	}
	public void setNextMessageId(String id) {
		nextMessageId = id;
	}
	@Override
	public String getMessageId(ChatMessage receivedChatMessage) {
		return nextMessageId;
	}
}