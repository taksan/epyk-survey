package skype.shell;


public interface ShellCommandProcessor {
	public boolean canProcess(ShellCommand command);
	public void process(ShellCommand command);
	public void setReplyListener(ReplyListener listener);
}
