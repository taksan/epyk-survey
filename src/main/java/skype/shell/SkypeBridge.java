package skype.shell;

import skype.ChatId;

public interface SkypeBridge {
	public void sendMessage(ChatId chatId, String message);
}
