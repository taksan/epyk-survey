package skype.voting.processors.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.ShellCommand;

public class VoteStatusRequest extends AbstractShellCommand implements ShellCommand {
	public VoteStatusRequest(ChatAdapterInterface chat, String command) {
		super(chat, command);
	}
}
