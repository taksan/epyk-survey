package skype.shell;

import skype.ChatId;

public interface ShellCommandFactory {
	public ShellCommand produce(ChatId chatId, String message);
}
