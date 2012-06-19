package skype.shell;

import skype.ChatAdapterInterface;
import skype.voting.CommandExecutor;

public class HelpCommandExecutor implements CommandExecutor {

	private final ShellCommandHelper[] helpers;
	private ReplyListener listener;
	
	public HelpCommandExecutor(ShellCommandHelper... helpers) {
		this.helpers = helpers;
	}

	@Override
	public boolean processMessage(ChatAdapterInterface chat, String message) {
		if (!isHelpRequest(message))
			return false;
		StringBuilder helpMessage = new StringBuilder();
		helpMessage.append(
				"------------------\n" +
				"Available commands\n");
		
		for (ShellCommandHelper h : helpers) {
			helpMessage.append("- " + h.getHelp()+"\n");
		}
		
		listener.onReplyPrivate(chat, helpMessage.toString());
		
		return true;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		this.listener = listener;
	}

	private boolean isHelpRequest(String message) {
		return message.trim().toLowerCase().equals("#help");
	}
}
