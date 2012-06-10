package skype.shell;

import skype.ChatAdapterInterface;

public abstract class AbstractShellCommand implements ShellCommand {
	private final String text;
	private final ChatAdapterInterface chat;
	protected AbstractShellCommand(ChatAdapterInterface chat, String command) {
		this.chat = chat;
		this.text = command;
		
	}
	public final String getText() {
		return text;
	}
	
	public final ChatAdapterInterface getChat() {
		return chat;
	}
}
