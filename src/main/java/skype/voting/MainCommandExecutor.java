package skype.voting;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;

public class MainCommandExecutor implements CommandExecutor {
	private final CommandExecutor[] processorUnits;

	public MainCommandExecutor(CommandExecutor[] processorUnits) {
		this.processorUnits = processorUnits;
	}

	@Override
	public boolean processMessage(ChatAdapterInterface chat, String message) {
		for (CommandExecutor pi : processorUnits) {
			if (pi.processMessage(chat, message))
				return true;
		}
		return false;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		for (CommandExecutor pi : processorUnits) {
			pi.setReplyListener(listener);
		}
	}
}