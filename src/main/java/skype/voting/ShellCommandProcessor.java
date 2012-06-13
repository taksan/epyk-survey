package skype.voting;

import skype.shell.ReplyListener;
import skype.shell.ShellCommand;

public interface ShellCommandProcessor {
	public boolean canProcess(ShellCommand command);
	public void process(ShellCommand command);
	public void setReplyListener(ReplyListener listener);
}
