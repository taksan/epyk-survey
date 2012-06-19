package skype.shell;


public interface ShellCommandProcessor {
	public void process(ShellCommand command);
	public void setReplyListener(ReplyListener listener);
}
