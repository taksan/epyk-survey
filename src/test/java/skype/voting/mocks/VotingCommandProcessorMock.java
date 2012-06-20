package skype.voting.mocks;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.voting.HelpVisitor;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;

public final class VotingCommandProcessorMock extends VotingCommandProcessorAbstract {
	public VotingCommandProcessorMock() {
		super(null);
	}

	public String processedMessage;
	
	@Override
	public boolean processMessage(ChatAdapterInterface chat, String message) {
		processedMessage = message;
		return true;
	}

	@Override
	public void process(ShellCommand command) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
	
	public void acceptHelpVisitor(HelpVisitor helpVisitor){
		helpVisitor.onTopic("VotingCommandProcessorMock visit");
	}
}