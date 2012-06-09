package skype.shell;

import skype.ChatAdapterInterface;

public interface ShellCommandFactory {
	public ShellCommand produce(ChatAdapterInterface chat, String message);
	public boolean understands(String message);
}
