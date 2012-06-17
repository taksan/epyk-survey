package skype.shell;

import skype.ChatAdapterInterface;

public interface ShellCommandInterpreter extends ShellCommandHelper, CommandInterpreter {
	public ShellCommand processMessage(ChatAdapterInterface chat, String message);
	
	public boolean understands(String message);
}
