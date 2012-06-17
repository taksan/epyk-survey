package skype.voting;

import java.util.LinkedHashMap;
import java.util.Map;


import skype.ChatAdapterInterface;
import skype.shell.CommandInterpreter;
import skype.shell.CommandInterpreterImpl;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandExecutor;
import skype.shell.ShellCommandProcessor;
import skype.voting.application.VotingSession;
import skype.voting.application.VotingSessionFactory;
import skype.voting.application.VotingSessionFactoryImpl;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.factories.VotingFactoriesRetriever;

public class VotingPollCommandExecutor extends ShellCommandExecutor implements CommandExecutor {

	private ReplyListener listener;
	final VotingSessionManager manager; 
	final Map<VotingSession, ChatListenerForVotingSession> listenersBySession = 
			new LinkedHashMap<VotingSession, ChatListenerForVotingSession>();
	
	VotingCommandProcessor[] processors = null;
	private final VotingSessionMessages voteSessionMessages;
	private final CommandInterpreter commandInterpreter;
	public VotingPollCommandExecutor() {
		this(new VotingSessionFactoryImpl(), new VotingSessionMessages(), new CommandInterpreterImpl());
	}
	
	VotingPollCommandExecutor(VotingSessionFactory votingSessionFactory, VotingSessionMessages voteSessionMessages,
			CommandInterpreter interpreter){
		this.voteSessionMessages = voteSessionMessages;
		commandInterpreter = interpreter;
		manager = new VotingSessionManager(votingSessionFactory);
	}
	
	public VotingPollCommandExecutor(
			VotingSessionFactory votingSessionFactory, 
			VotingSessionMessages votingSessionMessages,
			CommandInterpreter interpreter,
			VotingCommandProcessor[] commands) {
		this(votingSessionFactory, votingSessionMessages, interpreter);
		processors = commands;
	}

	@Override
	protected ShellCommandProcessor[] getProcessors() {
		if (processors != null) 
			return processors;
		
		processors = VotingFactoriesRetriever.getProcessors();
		for (VotingCommandProcessor aProcessor : processors) {
			aProcessor.setVoteSessionProvider(this);
			aProcessor.setVoteSessionMessages(voteSessionMessages);
		}
		
		return processors;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		super.setReplyListener(listener);
		this.listener = listener;
	}

	public VotingSession getSessionForRequest(ShellCommand request) {
		return manager.getSessionForRequest(request);
	}

	public VotingSession makeNewVotingSession(StartPollRequest votePollRequest) {
		VotingSession session = manager.makeNewVotingSession(votePollRequest);
		
		ChatAdapterInterface chat = votePollRequest.getChat();
		ChatListenerForVotingSession listenerForSession = 
				new ChatListenerForVotingSession(chat, session, listener, voteSessionMessages);
		listenersBySession.put(session, listenerForSession);
		
		return session;
	}

	public void removeSessionForGivenRequest(final ClosePollRequest request) {
		VotingSession votingSession = manager.getSessionForRequest(request);
		request.getChat().removeListener(listenersBySession.get(votingSession));
		listenersBySession.remove(votingSession);
		manager.removeSessionForRequest(request);
	}
	
	public boolean isInitializedSessionOnRequestChat(ShellCommand request) {
		 return manager.getSessionForRequest(request) != null;
	}

	@Override
	public boolean processMessage(ChatAdapterInterface chat, String message) {
		ShellCommand command = commandInterpreter.processMessage(chat, message);
		return processIfPossible(command);
	}
}