package skype.voting.processor.abstracts;

import skype.shell.CommandInterpreter;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.voting.VotingCommandProcessor;
import skype.voting.VotingSessionMessageInterface;
import skype.voting.VotingSessionModel;

public abstract class VotingCommandProcessorAbstract implements VotingCommandProcessor{
	private ReplyListener listener = null;
	protected VotingSessionModel executor;
	protected VotingSessionMessageInterface messages;

	@Override
	public void setReplyListener(ReplyListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void setVoteSessionMessages(VotingSessionMessageInterface voteSessionMessages){
		this.messages = voteSessionMessages;
		
	}
	
	public void setVoteSessionProvider(VotingSessionModel votingPollCommandProcessor) {
		this.executor = votingPollCommandProcessor;
	}
	
	protected ReplyListener getListener() {
		return listener;
	}
	
	protected void onReply(ShellCommand cmd, String reply) {
		listener.onReply(cmd.getChat(), reply);
	}	
	
	protected void onReplyPrivate(ShellCommand cmd, String reply) {
		listener.onReplyPrivate(cmd.getChat(), reply);
	}

	public void setInterpreter(CommandInterpreter commandInterpreter) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
}
