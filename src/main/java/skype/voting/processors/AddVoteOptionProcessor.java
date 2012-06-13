package skype.voting.processors;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.voting.VotingSession;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.AddVoteOptionRequest;

public class AddVoteOptionProcessor extends VotingCommandProcessorAbstract {

	@Override
	public boolean canProcess(ShellCommand command) {
		return command instanceof AddVoteOptionRequest;
	}

	@Override
	public void process(ShellCommand command) {
		if (!manager.isInitializedSessionOnRequestChat(command)) return;
		AddVoteOptionRequest request = (AddVoteOptionRequest)command;
		
		VotingSession votingSession = manager.getSessionForRequest(request);
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
				manager.getUpdatedVotingMenu(chat, votingSession).trim();
		onReply(command, reply);
	}

}
