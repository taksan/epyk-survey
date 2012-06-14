package skype.shell;


public interface ShellCommandExecutorInterface {

	public abstract boolean processIfPossible(ShellCommand aCommand);

	public abstract void setReplyListener(ReplyListener listener);

}