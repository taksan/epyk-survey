package skype.voting;

import java.util.LinkedHashMap;
import java.util.Map;

import skype.ChatAdapterInterface;
import skype.shell.CommandInterpreter;
import skype.shell.CommandInterpreterImpl;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandExecutor;
import skype.voting.application.VotingSession;
import skype.voting.application.VotingSessionFactory;
import skype.voting.application.VotingSessionFactoryImpl;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.factories.VotingFactoriesRetriever;

public class VotingPollCommandExecutor extends ShellCommandExecutor implements CommandExecutor, VotingSessionModel {

	private ReplyListener listener;
	final VotingSessionManager manager; 
	final Map<VotingSession, ChatListenerForVotingSession> listenersBySession = 
			new LinkedHashMap<VotingSession, ChatListenerForVotingSession>();
	
	VotingCommandProcessor[] processors = null;
	private final VotingSessionMessageInterface voteSessionMessages;
	private final CommandInterpreter commandInterpreter;
	public VotingPollCommandExecutor() {
		this(new VotingSessionFactoryImpl(), new VotingSessionMessages(), new CommandInterpreterImpl());
	}
	
	VotingPollCommandExecutor(VotingSessionFactory votingSessionFactory, VotingSessionMessageInterface voteSessionMessages,
			CommandInterpreter interpreter){
		this.voteSessionMessages = voteSessionMessages;
		commandInterpreter = interpreter;
		manager = new VotingSessionManager(votingSessionFactory);
	}
	
	public VotingPollCommandExecutor(
			VotingSessionFactory votingSessionFactory, 
			VotingSessionMessageInterface votingSessionMessages,
			CommandInterpreter interpreter,
			VotingCommandProcessor... commands) {
		this(votingSessionFactory, votingSessionMessages, interpreter);
		processors = commands;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		super.setReplyListener(listener);
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
	public boolean processMessage(ChatAdapterInterface chat, String message) {
		ShellCommand aCommand = commandInterpreter.processMessage(chat, message);
		
		for (VotingCommandProcessor p : getProcessors()) {
			if (p.canProcess(aCommand)) {
				p.process(aCommand);
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected VotingCommandProcessor[] getProcessors() {
		if (processors != null) 
			return processors;
		
		processors = VotingFactoriesRetriever.getProcessors();
		for (VotingCommandProcessor aProcessor : processors) {
			aProcessor.setVoteSessionProvider(this);
			aProcessor.setVoteSessionMessages(voteSessionMessages);
		}
		
		return processors;
	}
}