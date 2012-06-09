package skype.voting;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.skype.ChatListener;
import com.skype.User;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.CommandProcessor;
import skype.shell.ReplyListener;
import skype.shell.UnrecognizedCommand;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VotingPollRequest;

public class VotingPollProcessor implements CommandProcessor {

	private ReplyListener listener;
	final VotingSessionManager manager; 
	final Map<VotingSession, ChatListenerForVotingSession> listenersBySession = 
			new LinkedHashMap<VotingSession, ChatListenerForVotingSession>();
	
	public VotingPollProcessor() {
		this(new VotingSessionFactoryImpl());
	}
	
	VotingPollProcessor(VotingSessionFactory votingSessionFactory){
		manager = new VotingSessionManager(votingSessionFactory);
	}

	@Override
	public void processVotingPollRequest(VotingPollRequest votePollRequest) {
		VotingSession session = manager.makeNewVotingSession(votePollRequest);
		ChatListenerForVotingSession listenerForSession = new ChatListenerForVotingSession(votePollRequest.getChat(), session);
		listenersBySession.put(session, listenerForSession);
		
		String reply = buildVotingMenu(votePollRequest);
		listener.onReply(votePollRequest.getChat(), reply);
	}

	@Override
	public void processVoteRequest(VoteRequest request) {
		VotingSession votingSession = manager.getSessionForRequest(request);
		if (votingSession == null)
			return;
		votingSession.vote(request);
		
		String voteStatus = getVotingStatusMessage(votingSession);
		if (voteStatus.isEmpty())
			return;
		
		String reply = "Votes: " + voteStatus;
		listener.onReply(request.getChat(), reply);
	}

	@Override
	public void processClosePollRequest(final ClosePollRequest request) {
		final VotingSession votingSession = manager.getSessionForRequest(request);
		if (votingSession == null)
			return;
		
		votingSession.acceptWinnerConsultant(new WinnerConsultant() {
			@Override
			public void onWinner(VoteOptionAndCount winnerStats) {
				String voteStatus = getVotingStatusMessage(votingSession);
				String winnerMessage = 
						String.format("WINNER: ***%s*** with %d vote%s",
								winnerStats.optionName,
								winnerStats.voteCount,
								getPlural(winnerStats.voteCount)
								);
				String reply = "Votes: " + voteStatus + "\n" +	winnerMessage;
				removeSessionForGivenRequst(request);
				listener.onReply(request.getChat(), reply);
			}

			@Override
			public void onTie(Set<VotingPollOption> tiedOptions, int tieCount) {
				String voteStatus = getVotingStatusMessage(votingSession);
				StringBuilder winnerMessage = new StringBuilder();
				for (VotingPollOption option : tiedOptions) {
					winnerMessage.append(option.getName()+" and ");
				}
				
				String tiedOptionNames = winnerMessage.toString().replaceFirst(" and $", "");
				String reply = "Votes: " + voteStatus + "\n" +
						String.format("TIE: **%s** tied with %s vote%s",
								tiedOptionNames,
								tieCount,
								getPlural(tieCount)
								);
				
				removeSessionForGivenRequst(request);
				listener.onReply(request.getChat(), reply);
			}
			
			private String getPlural(Integer voteCount) {
				String string = voteCount>1?"s":"";
				return string;
			}
		});
	}
	
	@Override
	public void processUnrecognizedCommand(UnrecognizedCommand unrecognizedCommand) {
		if (!unrecognizedCommand.getText().startsWith("#"))
			return;
		
		listener.onReply(
				unrecognizedCommand.getChat(), 
				String.format("'%s' not recognized", unrecognizedCommand.getText()));
	}
	
	@Override
	public void addReplyListener(ReplyListener listener) {
		this.listener = listener;
	}

	protected String getVotingStatusMessage(VotingSession votingSession) {
		VotingStatusMessageFormatter formatter = new VotingStatusMessageFormatter();
		votingSession.acceptVoteConsultant(formatter);
		return formatter.getFormattedStatus();
	}

	private String buildVotingMenu(VotingPollRequest lunchRequest) {
		final StringBuffer msg = new StringBuffer();
		lunchRequest.accept(new VotingPollVisitor() {
			int count=1;
			int voterCount=0;
			@Override
			public void onWelcomeMessage(String message) {
				msg.append(message+"\n");
			}
			@Override
			public void visitOption(VotingPollOption option) {
				msg.append(count+") ");
				msg.append(option.getName());
				msg.append("\n");
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
		String reply = "\n"+StringUtils.substring(msg.toString(),0,-1);
		return reply;
	}

	void onReply(ChatAdapterInterface chat, String reply) {
		listener.onReply(chat, reply);
	}

	private void removeSessionForGivenRequst(final ClosePollRequest request) {
		VotingSession votingSession = manager.getSessionForRequest(request);
		request.getChat().removeListener(listenersBySession.get(votingSession));
		listenersBySession.remove(votingSession);
		manager.removeSessionForRequest(request);
		System.gc();
	}
	
	class ChatListenerForVotingSession implements ChatListener {

		private final VotingSession targetSession;
		private final ChatAdapterInterface chat;
		private SkypeBridge bridge;
		
		public ChatListenerForVotingSession(ChatAdapterInterface chat, VotingSession delegate) {
			this.chat = chat;
			this.targetSession = delegate;
			this.bridge = chat.getSkypeBridge();
			chat.addListener(this);
		}

		@Override
		public void userAdded(User user) {
			String participant = bridge.getUserFullNameOrId(user);
			targetSession.addNewParticipant(participant);
			onReply(chat, String.format("User '%s' added to the voting poll.", participant));
		}

		@Override
		public void userLeft(User user) {
			String participant = bridge.getUserFullNameOrId(user);
			targetSession.removeParticipant(participant);
			
			String status = getVotingStatusMessage(targetSession);
			onReply(chat, String.format("User '%s' left the voting poll.\n" +
					"Update Votes: "+status, 
					participant));
		}
	}
}
