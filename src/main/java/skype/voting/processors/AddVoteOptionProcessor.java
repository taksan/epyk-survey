package skype.voting.processors;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.voting.application.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.processors.interpreters.AddVoteOptionRequestInterpreter;
import skype.voting.processors.requests.AddVoteOptionRequest;

public class AddVoteOptionProcessor extends VotingCommandProcessorAbstract {
	
	public AddVoteOptionProcessor() {
		super(new AddVoteOptionRequestInterpreter());
	}
	
	@Override
	public void process(ShellCommand command) {
		if (!votingModel.isInitializedSessionOnRequestChat(command)) return;
		AddVoteOptionRequest request = (AddVoteOptionRequest)command;
		
		VotingSession votingSession = votingModel.getSessionForRequest(request);
		ChatAdapterInterface chat = request.getChat();
		boolean added = votingSession.addOption(request.getName());
		if (!added) {
			onReplyPrivate(command, "Option '"+request.getName()+"' already added.");
			return;
		}
		String header = String.format("New option '%s' added by %s. Current options:\n", 
				request.getName(),chat.getLasterSenderFullName());
		
		String reply =
				header+
				messages.getUpdatedVotingMenu(votingSession).trim();
		onReply(command, reply);
	}

}
