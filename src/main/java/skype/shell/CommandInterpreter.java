package skype.shell;

import skype.ChatAdapterInterface;

public interface CommandInterpreter {
	public ShellCommand processMessage(ChatAdapterInterface chat, String message);
}
