package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandProcessor;
import skype.shell.ShellCommand;

public class AddVoteOptionRequest extends AbstractShellCommand implements ShellCommand {
	private final String optionName;

	public AddVoteOptionRequest(ChatAdapterInterface chat, String command, String optionName) {
		super(chat, command);
		this.optionName = optionName;
	}

	@Override
	public void beProcessedAsSentMessage(CommandProcessor processor) {
		processor.processAddVoteOption(this);
	}

	@Override
	public void beProcessedAsReceivedMessage(CommandProcessor processor) {
		processor.processAddVoteOption(this);
	}

	public String getName() {
		return optionName;
	}

}
