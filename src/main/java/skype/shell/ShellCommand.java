package skype.shell;


public interface ShellCommand {
	public String getText();

	public void acceptSentRequest(ShellCommandVisitor visitor);
}
