package skype.voting.mocks;

import skype.ChatAdapterInterface;
import skype.alias.HelpProvider;
import skype.shell.ReplyListener;
import skype.voting.CommandExecutor;
import skype.voting.HelpVisitor;

public class CommmandExecutorAndHelperMock implements CommandExecutor, HelpProvider {

	private final StringBuilder operations;
	private final String id;

	public CommmandExecutorAndHelperMock(String id, StringBuilder operations) {
		this.id = id;
		this.operations = operations;
	}

	@Override
	public void acceptHelpVisitor(HelpVisitor helpVisitor) {
		this.operations.append(id+" acceptHelpVisitor\n");
	}

	@Override
	public boolean processMessage(ChatAdapterInterface chat, String message) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		operations.append(id+" CommmandExecutorAndHelperMock setReplyListener\n");
	}

}
