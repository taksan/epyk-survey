package skype.voting.processor.abstracts;

import skype.ChatAdapterInterface;
import skype.shell.CommandInterpreter;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.voting.CommandExecutor;
import skype.voting.VotingCommandProcessor;
import skype.voting.VotingSessionMessageInterface;
import skype.voting.VotingSessionModel;

public abstract class VotingCommandProcessorAbstract implements VotingCommandProcessor, CommandExecutor{
	private ReplyListener listener = null;
	protected VotingSessionModel executor;
	protected VotingSessionMessageInterface messages;
	private CommandInterpreter interpreter;

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
		this.interpreter = commandInterpreter;
		
	}
	
	public boolean processMessage(ChatAdapterInterface chat, String message){
		ShellCommand aCommand = this.interpreter.processMessage(chat, message);
		if (canProcess(aCommand)) {
			process(aCommand);
			return true;
		}
		return false;
	}
}
