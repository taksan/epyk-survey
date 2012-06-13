package skype.voting;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.VotingPollVisitor;
import skype.voting.requests.factories.VotingFactoriesRetriever;

public class VotingPollCommandExecutor extends ShellCommandExecutor {

	private ReplyListener listener;
	final VotingSessionManager manager; 
	final Map<VotingSession, ChatListenerForVotingSession> listenersBySession = 
			new LinkedHashMap<VotingSession, ChatListenerForVotingSession>();
	
	VotingCommandProcessor[] processors = null;
	public VotingPollCommandExecutor() {
		this(new VotingSessionFactoryImpl());
	}
	
	@Override
	protected ShellCommandProcessor[] getProcessors() {
		if (processors != null) 
			return processors;
		
		processors = VotingFactoriesRetriever.getProcessors();
		for (VotingCommandProcessor aProcessor : processors) {
			aProcessor.setVoteSessionProvider(this);
		}
		
		return processors;
	}
	
	VotingPollCommandExecutor(VotingSessionFactory votingSessionFactory){
		manager = new VotingSessionManager(votingSessionFactory);
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
				new ChatListenerForVotingSession(chat, session, listener);
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
	
	public static String getVotingStatusMessage(VotingSession votingSession) {
		VotingStatusMessageFormatter formatter = new VotingStatusMessageFormatter();
		votingSession.acceptVoteConsultant(formatter);
		return formatter.getFormattedStatus();
	}

	public static String buildVotingMenu(VotingSession session) {
		final StringBuffer msg = new StringBuffer();
		
		session.accept(new VotingPollVisitor() {
			int count=1;
			int voterCount=0;
			@Override
			public void onWelcomeMessage(String message) {
				msg.append(message+"\n");
			}
			@Override
			public void visitOption(VotingPollOption option) {
				String optionMsg = count+") "+option.getName();
				msg.append(optionMsg+"\n");
				
				count++;
			}
			@Override
			public void visitParticipant(String participantName) {
				if (voterCount == 0)
					msg.append("Voters: ");
				msg.append(participantName+",");
				voterCount++;
			}
		});
		return "\n"+StringUtils.substring(msg.toString(),0,-1)+"\n";
	}

	public static String buildVotingMenuWithoutVoters(VotingSession targetSession) {
		String buildGuidelineText = buildVotingMenu(targetSession);
		return buildGuidelineText.replaceAll("Voters:.*", "");
	}

	public String getUpdatedVotingMenu(VotingSession session) {
		String reply = buildVotingMenu(session);
		return reply;
	}
}