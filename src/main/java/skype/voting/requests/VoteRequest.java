package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandProcessor;
import skype.shell.mocks.ChatBridgeMock;

public class VoteRequest extends AbstractShellCommand {

	public final int vote;
	public final String sender;

	VoteRequest(ChatAdapterInterface chat, String command, int vote) {
		super(chat, command);
		this.vote = vote;
		this.sender = chat.getSenderFullName();
	}
	
	public VoteRequest(String user, int vote) {
		this(new ChatBridgeMock("autoid", user), "#"+vote, vote);
	}

	@Override
	public void acceptProcessorForSentMessages(CommandProcessor visitor) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void acceptProcessorForReceivedMessages(CommandProcessor processor) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
}
