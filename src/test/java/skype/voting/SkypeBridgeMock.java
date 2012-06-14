package skype.voting;

import java.util.List;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;

import com.skype.ChatListener;
import com.skype.ChatMessage;
import com.skype.User;

final class SkypeBridgeMock implements SkypeBridge {
	private final StringBuilder operations;

	SkypeBridgeMock(StringBuilder operations) {
		this.operations = operations;
		
	}
	@Override
	public void sendMessage(ChatAdapterInterface chatBridgeInterface, String message) {
		operations.append("SkypeBridge: sendMessage: " + message);
	}

	@Override
	public String getUserFullNameOrId(User sender) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public ChatAdapterInterface getChatAdapter(ChatMessage sentChatMessage) {
		operations.append("getChatAdapter\n");
		return new ChatAdapterInterface() {
			
			@Override
			public void setTopic(String topic) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void setLastSender(String senderFullNameOrId) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void setGuidelines(String guidelines) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void removeListener(ChatListener weakReference) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public SkypeBridge getSkypeBridge() {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public List<String> getPartipantNames() {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public String getLasterSenderFullName() {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void addListener(ChatListener listener) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
		};
	}

	@Override
	public String getContent(ChatMessage receivedChatMessage) {
		operations.append("getContent\n");
		return "";
	}

	@Override
	public void sendMessageToUser(String user, String message) {
		operations.append("sendMessageToUser: " +user +" message:" + message);
	}
}