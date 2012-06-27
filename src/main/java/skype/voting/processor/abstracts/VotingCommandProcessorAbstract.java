package skype.voting.processor.abstracts;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandInterpreter;
import skype.voting.CommandExecutor;
import skype.voting.HelpVisitor;
import skype.voting.VotingCommandProcessor;
import skype.voting.VotingSessionMessageInterface;
import skype.voting.VotingSessionModel;

public abstract class VotingCommandProcessorAbstract implements VotingCommandProcessor, CommandExecutor{
	private ReplyListener listener = null;
	protected VotingSessionModel votingModel;
	protected VotingSessionMessageInterface messages;
	private ShellCommandInterpreter interpreter;
	
	protected VotingCommandProcessorAbstract(ShellCommandInterpreter interpreter) {
		this.interpreter = interpreter;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void setVoteSessionMessages(VotingSessionMessageInterface voteSessionMessages){
		this.messages = voteSessionMessages;
		
	}
	
	public void setVoteSessionProvider(VotingSessionModel votingPollCommandProcessor) {
		this.votingModel = votingPollCommandProcessor;
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
	
	@Override
	public boolean processMessage(ChatAdapterInterface chat, String message){
		ShellCommand aCommand = this.interpreter.processMessage(chat, message);
		if (aCommand ==null)
			return false;
		process(aCommand);
		return true;
	}
	
	@Override
	public void acceptHelpVisitor(HelpVisitor helpVisitor) {
		helpVisitor.onTopic(interpreter.getHelp());
	}
}
