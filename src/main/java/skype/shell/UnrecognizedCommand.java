package skype.shell;

import skype.ChatAdapterInterface;

public class UnrecognizedCommand implements ShellCommand {

	private final String unrecognizedCommand;
	private final ChatAdapterInterface chat;

	public UnrecognizedCommand(ChatAdapterInterface chat, String unrecognizedCommand) {
		this.chat = chat;
		this.unrecognizedCommand = unrecognizedCommand;
	}
	
	@Override
	public String getText() {
		return this.unrecognizedCommand;
	}
	
	@Override
	public void acceptProcessorForSentMessages(CommandProcessor processor) {
		processor.processUnrecognizedCommand(this);
	}
	
	@Override
	public void acceptProcessorForReceivedMessages(CommandProcessor processor) {
		processor.processUnrecognizedCommand(this);
	}

	@Override
	public ChatAdapterInterface getChat() {
		return chat;
	}

}
