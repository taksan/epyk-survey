package skype.voting;

import skype.shell.ReplyListener;
import skype.shell.ShellCommand;

public interface ShellCommandExecutorInterface {

	public abstract boolean processIfPossible(ShellCommand aCommand);

	public abstract void setReplyListener(ReplyListener listener);

}