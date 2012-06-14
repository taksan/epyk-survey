package skype.voting;

import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandExecutorInterface;

final class CommandProcessorAdapterMock implements ShellCommandExecutorInterface {
	public ReplyListener listener;
	StringBuilder operations;
	CommandProcessorAdapterMock(StringBuilder op){
		operations = op;
	}

	public void setReplyListener(ReplyListener listener) {
		operations.append("ShellCommandExecutor setReplyListener\n");
		this.listener = listener;
	}

	@Override
	public boolean processIfPossible(ShellCommand aCommand) {
		operations.append("ShellCommandExecutor processIfPossible\n");
		return true;
	}
}