package skype.voting;

import skype.ChatAdapterInterface;
import skype.shell.CommandInterpreter;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;

public class CommandExecutorImpl implements CommandExecutor {
	private final ShellCommandExecutorInterface processor;
	private final CommandInterpreter interpreter;

	public CommandExecutorImpl(CommandInterpreter interpreter, ShellCommandExecutorInterface processor) {
		this.processor = processor;
		this.interpreter = interpreter;
	}

	public CommandExecutorImpl(CommandProcessor[] commandProcessors) {
		this.processor = null;
		this.interpreter = null;
	}

	@Override
	public boolean processMessage(ChatAdapterInterface chat, String message) {
		ShellCommand shellCommand = interpreter.processMessage(
				chat,
				message);
		
		processor.processIfPossible(shellCommand);
		return true;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		processor.setReplyListener(listener);
	}
}