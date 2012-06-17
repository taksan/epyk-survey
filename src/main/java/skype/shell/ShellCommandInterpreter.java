package skype.shell;

import skype.ChatAdapterInterface;

public interface ShellCommandInterpreter extends ShellCommandHelper {
	public ShellCommand produce(ChatAdapterInterface chat, String message);
	public boolean understands(String message);
}
