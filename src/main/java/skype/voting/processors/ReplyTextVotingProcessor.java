package skype.voting.processors;

import skype.shell.ReplyTextRequest;
import skype.shell.ShellCommand;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;

public class ReplyTextVotingProcessor extends VotingCommandProcessorAbstract {

	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof ReplyTextRequest;
	}

	@Override
	public void process(ShellCommand command) {
		ReplyTextRequest request = (ReplyTextRequest) command;
		onReply(command, request.getReplyText());
	}

}
