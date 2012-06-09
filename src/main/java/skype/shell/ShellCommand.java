package skype.shell;

import skype.ChatAdapterInterface;


public interface ShellCommand {
	public String getText();
	public ChatAdapterInterface getChat();

	public void acceptProcessorForSentMessages(CommandProcessor processor);
	public void acceptProcessorForReceivedMessages(CommandProcessor processor);
}
