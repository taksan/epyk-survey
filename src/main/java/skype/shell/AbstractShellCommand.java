package skype.shell;

import skype.ChatId;

public abstract class AbstractShellCommand implements ShellCommand {
	private final String text;
	private final ChatId chatId;
	protected AbstractShellCommand(ChatId chatId, String command) {
		this.chatId = chatId;
		this.text = command;
		
	}
	public String getText() {
		return text;
	}
	
	public ChatId getChatId() {
		return chatId;
	}
}
