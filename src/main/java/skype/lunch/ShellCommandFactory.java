package skype.lunch;

import skype.ChatId;
import skype.shell.ShellCommand;

public interface ShellCommandFactory {
	public ShellCommand produce(ChatId chatId, String message);
}
