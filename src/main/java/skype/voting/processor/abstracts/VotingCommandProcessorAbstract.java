package skype.voting.processor.abstracts;

import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.voting.VotingCommandProcessor;
import skype.voting.VotingPollCommandProcessor;

public abstract class VotingCommandProcessorAbstract implements VotingCommandProcessor{
	private ReplyListener listener = null;
	protected VotingPollCommandProcessor manager;

	@Override
	public void setReplyListener(ReplyListener listener) {
		this.listener = listener;
	}
	
	public void setVoteSessionProvider(VotingPollCommandProcessor votingPollCommandProcessor) {
		this.manager = votingPollCommandProcessor;
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
