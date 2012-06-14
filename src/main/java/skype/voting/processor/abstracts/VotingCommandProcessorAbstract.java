package skype.voting.processor.abstracts;

import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.voting.VotingCommandProcessor;
import skype.voting.VotingPollCommandExecutor;
import skype.voting.VotingSessionMessages;

public abstract class VotingCommandProcessorAbstract implements VotingCommandProcessor{
	private ReplyListener listener = null;
	protected VotingPollCommandExecutor executor;
	protected VotingSessionMessages messages;

	@Override
	public void setReplyListener(ReplyListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void setVoteSessionMessages(VotingSessionMessages voteSessionMessages){
		this.messages = voteSessionMessages;
		
	}
	
	public void setVoteSessionProvider(VotingPollCommandExecutor votingPollCommandProcessor) {
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
}
