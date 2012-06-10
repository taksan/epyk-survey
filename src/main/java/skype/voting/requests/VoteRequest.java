package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandProcessor;
import skype.shell.ShellCommand;
import skype.shell.mocks.ChatBridgeMock;

public class VoteRequest extends AbstractShellCommand implements ShellCommand {

	public final int vote;
	public final String sender;

	public VoteRequest(ChatAdapterInterface chat, String command, int vote) {
		super(chat, command);
		this.vote = vote;
		this.sender = chat.getLasterSenderFullName();
	}
	public VoteRequest(ChatAdapterInterface chat, String command){
		this(chat,command,0);
	}
	
	public VoteRequest(String user, int vote) {
		this(new ChatBridgeMock("autoid", user), "#"+vote, vote);
	}

	@Override
	public void beProcessedAsSentMessage(CommandProcessor processor) {
		processor.processVoteRequest(this);
	}

	@Override
	public void beProcessedAsReceivedMessage(CommandProcessor processor) {
		processor.processVoteRequest(this);
	}
}
