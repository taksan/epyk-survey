package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.ShellCommand;

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
}
