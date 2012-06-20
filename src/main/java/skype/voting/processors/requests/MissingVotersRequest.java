package skype.voting.processors.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;

public class MissingVotersRequest extends AbstractShellCommand {

	public MissingVotersRequest(ChatAdapterInterface chat, String command) {
		super(chat, command);
	}
}