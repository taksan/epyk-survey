package skype.voting;

import java.util.LinkedHashMap;
import java.util.Map;

import skype.ChatAdapterInterface;
import skype.alias.HelpProvider;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandProcessor;
import skype.voting.application.VotingSession;
import skype.voting.application.VotingSessionFactory;
import skype.voting.application.VotingSessionFactoryImpl;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import skype.voting.processors.interpreters.VotingFactoriesRetriever;
import skype.voting.processors.requests.ClosePollRequest;
import skype.voting.processors.requests.StartPollRequest;

public class VotingPollCommandExecutor implements CommandExecutor, VotingSessionModel, HelpProvider {

	final Map<VotingSession, ChatListenerForVotingSession> listenersBySession = 
			new LinkedHashMap<VotingSession, ChatListenerForVotingSession>();
	
	private ReplyListener listener;
	private final VotingSessionManager manager; 
	
	private VotingCommandProcessorAbstract[] processors = null;
	private final VotingSessionMessageInterface voteSessionMessages;
	boolean areProcessorsInitialized = false;
	
	public VotingPollCommandExecutor() {
		this(new VotingSessionFactoryImpl(), 
			 new VotingSessionMessages());
	}
	
	VotingPollCommandExecutor(VotingSessionFactory votingSessionFactory, VotingSessionMessageInterface voteSessionMessages){
		this.voteSessionMessages = voteSessionMessages;
		manager = new VotingSessionManager(votingSessionFactory);
	}
	
	public VotingPollCommandExecutor(
			VotingSessionFactory votingSessionFactory, 
			VotingSessionMessageInterface votingSessionMessages,
			VotingCommandProcessorAbstract... commands) {
		this(votingSessionFactory, votingSessionMessages);
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
		for (VotingCommandProcessorAbstract p : getProcessors()) {
			if (p.processMessage(chat, messageToProcess)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void acceptHelpVisitor(HelpVisitor helpVisitor) {
		helpVisitor.onTopLevel("Available voting commands");
		for (VotingCommandProcessorAbstract aProcessor : processors) {
			aProcessor.acceptHelpVisitor(helpVisitor);
		}
	}
	
	protected VotingCommandProcessorAbstract[] getProcessors() {
		if (processors == null) {
			processors = VotingFactoriesRetriever.getProcessors();
		}
		
		initializeProcessors();
		
		return processors;
	}

	private void initializeProcessors() {
		if (areProcessorsInitialized)
			return;
		
		for (VotingCommandProcessorAbstract aProcessor : processors) {
			aProcessor.setVoteSessionProvider(this);
			aProcessor.setVoteSessionMessages(voteSessionMessages);
		}
		areProcessorsInitialized = true;
	}

}