package lunch.main;

import skype.ChatId;
import skype.shell.SkypeBridge;

public class SkypeBridgeImpl implements SkypeBridge {

	@Override
	public void sendMessage(ChatId chatId, String message) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
}
