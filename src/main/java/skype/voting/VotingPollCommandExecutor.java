package skype.voting;

import java.util.LinkedHashMap;
import java.util.Map;

import skype.ChatAdapterInterface;
import skype.alias.AliasExpander;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandProcessor;
import skype.voting.application.VotingSession;
import skype.voting.application.VotingSessionFactory;
import skype.voting.application.VotingSessionFactoryImpl;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.factories.VotingFactoriesRetriever;

public class VotingPollCommandExecutor implements CommandExecutor, VotingSessionModel {

	private ReplyListener listener;
	final VotingSessionManager manager; 
	final Map<VotingSession, ChatListenerForVotingSession> listenersBySession = new LinkedHashMap<VotingSession, ChatListenerForVotingSession>();
	VotingCommandProcessorAbstract[] processors = null;
	private final VotingSessionMessageInterface voteSessionMessages;
	private final AliasExpander aliasExpander;
	
	public VotingPollCommandExecutor() {
		this(new VotingSessionFactoryImpl(), 
			 new VotingSessionMessages(), 
			 VotingFactoriesRetriever.getSingletonAliasExpander());
	}
	
	VotingPollCommandExecutor(VotingSessionFactory votingSessionFactory, VotingSessionMessageInterface voteSessionMessages,
			AliasExpander aliasExpander){
		this.voteSessionMessages = voteSessionMessages;
		this.aliasExpander = aliasExpander;
		manager = new VotingSessionManager(votingSessionFactory);
	}
	
	public VotingPollCommandExecutor(
			VotingSessionFactory votingSessionFactory, 
			VotingSessionMessageInterface votingSessionMessages,
			AliasExpander aliasExpander,
			VotingCommandProcessorAbstract... commands) {
		this(votingSessionFactory, votingSessionMessages, aliasExpander);
		processors = commands;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		for (ShellCommandProcessor p : getProcessors()) {
			p.setReplyListener(listener);
		}
		this.listener = listener;
	}

	@Override
	public VotingSession getSessionForRequest(ShellCommand request) {
		return manager.getSessionForRequest(request);
	}

	@Override
	public VotingSession makeNewVotingSession(StartPollRequest votePollRequest) {
		VotingSession session = manager.makeNewVotingSession(votePollRequest);
		
		ChatAdapterInterface chat = votePollRequest.getChat();
		ChatListenerForVotingSession listenerForSession = 
				new ChatListenerForVotingSession(chat, session, listener, voteSessionMessages);
		listenersBySession.put(session, listenerForSession);
		
		return session;
	}

	@Override
	public void removeSessionForGivenRequest(final ClosePollRequest request) {
		VotingSession votingSession = manager.getSessionForRequest(request);
		request.getChat().removeListener(listenersBySession.get(votingSession));
		listenersBySession.remove(votingSession);
		manager.removeSessionForRequest(request);
	}
	
	@Override
	public boolean isInitializedSessionOnRequestChat(ShellCommand request) {
		 return manager.getSessionForRequest(request) != null;
	}

	@Override
	public boolean processMessage(ChatAdapterInterface chat, String messageToProcess) {
		String expandedMessage = aliasExpander.expand(messageToProcess);
		for (VotingCommandProcessorAbstract p : getProcessors()) {
			if (p.processMessage(chat, expandedMessage)) {
				return true;
			}
		}
		return false;
	}
	
	boolean areProcessorsInitialized = false;
	
	protected VotingCommandProcessorAbstract[] getProcessors() {
		if (processors != null) {
			if (!areProcessorsInitialized) {
				initializeProcessors();
			}
			return processors;
		}
		
		processors = VotingFactoriesRetriever.getProcessors();
		
		initializeProcessors();
		
		return processors;
	}

	private void initializeProcessors() {
		for (VotingCommandProcessorAbstract aProcessor : processors) {
			aProcessor.setVoteSessionProvider(this);
			aProcessor.setVoteSessionMessages(voteSessionMessages);
		}
		areProcessorsInitialized = true;
	}
}