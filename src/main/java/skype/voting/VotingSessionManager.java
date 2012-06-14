package skype.voting;

import java.util.LinkedHashMap;
import java.util.Map;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.voting.application.VotingSession;
import skype.voting.application.VotingSessionFactory;
import skype.voting.requests.StartPollRequest;

public class VotingSessionManager {
	private final VotingSessionFactory votingSessionFactory;
	Map<ChatAdapterInterface, VotingSession> sessionByChat = new LinkedHashMap<ChatAdapterInterface, VotingSession>();
	public VotingSessionManager(VotingSessionFactory votingSessionFactory) {
		this.votingSessionFactory = votingSessionFactory;
	}

	public VotingSession makeNewVotingSession(StartPollRequest request) {
		VotingSession votingSession = votingSessionFactory.produce();
		
		votingSession.initWith(request);
		sessionByChat.put(request.getChat(), votingSession);
		return votingSession;
	}
	
	public void removeSessionForRequest(ShellCommand request) {
		sessionByChat.remove(request.getChat());
	}


	public VotingSession getSessionForRequest(ShellCommand request2) {
		return sessionByChat.get(request2.getChat());
	}

}
