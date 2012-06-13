package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.ShellCommand;

public class ClosePollRequest extends AbstractShellCommand implements ShellCommand {

	public ClosePollRequest(ChatAdapterInterface chat, String command) {
		super(chat, command);
	}
}
