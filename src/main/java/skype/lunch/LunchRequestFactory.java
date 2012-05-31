package skype.lunch;

import skype.ChatId;

public class LunchRequestFactory implements ShellCommandFactory {

	@Override
	public LunchRequest produce(ChatId chatId, String message) {
		if (understands(message))
			return new LunchRequest(chatId, message);
		return null;
	}

	private boolean understands(String message) {
		return message.startsWith("#lunch");
	} 
	
}