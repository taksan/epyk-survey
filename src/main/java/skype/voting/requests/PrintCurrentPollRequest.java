package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;

public class PrintCurrentPollRequest extends AbstractShellCommand {

	public PrintCurrentPollRequest(ChatAdapterInterface chat, String message) {
		super(chat,message);
	}
}
