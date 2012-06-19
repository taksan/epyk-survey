package skype.voting.processors;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.voting.application.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.AddVoteOptionRequest;
import skype.voting.requests.factories.AddVoteOptionRequestInterpreter;

public class AddVoteOptionProcessor extends VotingCommandProcessorAbstract {
	
	public AddVoteOptionProcessor() {
		super(new AddVoteOptionRequestInterpreter());
	}
	
	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof AddVoteOptionRequest;
	}

	@Override
	public void process(ShellCommand command) {
		if (!executor.isInitializedSessionOnRequestChat(command)) return;
		AddVoteOptionRequest request = (AddVoteOptionRequest)command;
		
		VotingSession votingSession = executor.getSessionForRequest(request);
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
