package skype.shell;


public abstract class ShellCommandExecutor implements ShellCommandExecutorInterface  {
	protected abstract ShellCommandProcessor[] getProcessors();

	@Override
	public boolean processIfPossible(ShellCommand aCommand) {
		for (ShellCommandProcessor p : getProcessors()) {
			if (p.canProcess(aCommand)) {
				p.process(aCommand);
				return true;
			}
		}
		return false;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		for (ShellCommandProcessor p : getProcessors()) {
			p.setReplyListener(listener);
		}
	}
}
