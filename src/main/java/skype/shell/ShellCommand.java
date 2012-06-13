package skype.shell;

import skype.ChatAdapterInterface;


public interface ShellCommand {
	public String getText();
	public ChatAdapterInterface getChat();
}
