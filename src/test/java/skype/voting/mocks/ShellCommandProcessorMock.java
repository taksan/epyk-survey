package skype.voting.mocks;

import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.voting.ShellCommandProcessor;

public class ShellCommandProcessorMock implements ShellCommandProcessor {

	private boolean canProcess;
	private boolean wasProcessed;
	private String publicReply = null;
	private ReplyListener listener;

	public ShellCommandProcessorMock(boolean b) {
		canProcess = b;
	}

	@Override
	public boolean canProcess(ShellCommand command) {
		return canProcess;
	}

	@Override
	public void process(ShellCommand command) {
		wasProcessed = true;
		if (publicReply != null)
			this.listener.onReply(null, publicReply);
	}
	
	@Override
	public void setReplyListener(ReplyListener listener) {
		this.listener = listener;
	}

	public boolean isProcessed() {
		return wasProcessed;
	}

	public void setPublicReplyMessage(String publicReply) {
		this.publicReply  = publicReply;
		
	}
}
