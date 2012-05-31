package skype.lunch.mocks;

import skype.ChatId;
import skype.SkypeBridge;

public final class SkypeBridgeMock implements SkypeBridge {
	public String sentMessage="";
	public String toChatId="";
	
	@Override
	public void sendMessage(ChatId chatId, String message) {
		this.sentMessage = message;
		this.toChatId = chatId.chatId;
	}
}