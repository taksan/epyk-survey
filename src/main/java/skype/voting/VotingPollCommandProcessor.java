package skype.voting;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.CommandProcessor;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.shell.UnrecognizedCommand;
import skype.voting.requests.AddVoteOptionRequest;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.MissingVotersRequest;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VoteStatusRequest;
import skype.voting.requests.VotingPollVisitor;

import com.skype.ChatListener;
import com.skype.User;

public class VotingPollCommandProcessor implements CommandProcessor {

	private ReplyListener listener;
	final VotingSessionManager manager; 
	final Map<VotingSession, ChatListenerForVotingSession> listenersBySession = 
			new LinkedHashMap<VotingSession, ChatListenerForVotingSession>();
	
	public VotingPollCommandProcessor() {
		this(new VotingSessionFactoryImpl());
	}
	
	VotingPollCommandProcessor(VotingSessionFactory votingSessionFactory){
		manager = new VotingSessionManager(votingSessionFactory);
	}

	@Override
	public void processVotingPollRequest(StartPollRequest votePollRequest) {
		VotingSession session = manager.makeNewVotingSession(votePollRequest);
		ChatListenerForVotingSession listenerForSession = 
				new ChatListenerForVotingSession(votePollRequest.getChat(), session);
		listenersBySession.put(session, listenerForSession);
		
		String reply = buildVotingMenu(votePollRequest.getChat(), session);
		onReply(votePollRequest.getChat(), reply);
	}

	@Override
	public void processVoteRequest(VoteRequest request) {
		if (!isInitializedSessionOnRequestChat(request)) return;
		
		VotingSession votingSession = manager.getSessionForRequest(request);
		votingSession.vote(request);
		
		String voteStatus = getVotingStatusMessage(votingSession);
		if (voteStatus.isEmpty())
			return;
		
		String reply = "Votes: " + voteStatus;
		listener.onReply(request.getChat(), reply);
	}

	@Override
	public void processClosePollRequest(final ClosePollRequest request) {
		if (!isInitializedSessionOnRequestChat(request)) return;
		
		final VotingSession votingSession = manager.getSessionForRequest(request);
		
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
				removeSessionForGivenRequest(request);
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
				
				removeSessionForGivenRequest(request);
				listener.onReply(request.getChat(), reply);
			}
			
			private String getPlural(Integer voteCount) {
				String string = voteCount>1?"s":"";
				return string;
			}
		});
	}
	
	@Override
	public void processVoteStatusRequest(VoteStatusRequest request) {
		if (!isInitializedSessionOnRequestChat(request)) return;
		
		final VotingSession votingSession = manager.getSessionForRequest(request);
		String status = "Votes: "+getVotingStatusMessage(votingSession);
		onReplyPrivate(request.getChat(), status);
	}
	
	@Override
	public void processHelpCommand(HelpRequest request) {
		if (!isInitializedSessionOnRequestChat(request)) return;
		
		String helpMessage = request.getHelpMessage()+"\n" +
				"Also, you can use '/get guidelines' to see the available voting options";
		onReplyPrivate(request.getChat(), helpMessage);
	}	
	

	@Override
	public void processUnrecognizedCommand(UnrecognizedCommand unrecognizedCommand) {
		if (!unrecognizedCommand.getText().startsWith("#"))
			return;
		
		listener.onReplyPrivate(
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

	private String buildVotingMenu(ChatAdapterInterface chat, VotingSession session) {
		final StringBuffer msg = new StringBuffer();
		final StringBuffer guideline = new StringBuffer();
		guideline.append("Poll ");
		
		session.accept(new VotingPollVisitor() {
			int count=1;
			int voterCount=0;
			@Override
			public void onWelcomeMessage(String message) {
				msg.append(message+"\n");
				guideline.append("'"+message+"' undergoing. Options: ");
			}
			@Override
			public void visitOption(VotingPollOption option) {
				String optionMsg = count+") "+option.getName();
				msg.append(optionMsg+"\n");
				guideline.append(optionMsg+" "); 
				
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
		chat.setGuidelines(guideline.toString().trim());
		String reply = "\n"+StringUtils.substring(msg.toString(),0,-1)+"\n";
		return reply;
	}

	private void onReply(ChatAdapterInterface chat, String reply) {
		listener.onReply(chat, reply);
	}
	
	private void onReplyPrivate(ChatAdapterInterface chat, String reply) {
		listener.onReplyPrivate(chat, reply);
	}

	private void removeSessionForGivenRequest(final ClosePollRequest request) {
		VotingSession votingSession = manager.getSessionForRequest(request);
		request.getChat().removeListener(listenersBySession.get(votingSession));
		listenersBySession.remove(votingSession);
		manager.removeSessionForRequest(request);
	}
	
	private boolean isInitializedSessionOnRequestChat(ShellCommand request) {
		 return manager.getSessionForRequest(request) != null;
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
			String status = getVotingStatusMessage(targetSession);
			onReply(chat, 
					String.format(
							"User '%s' added to the voting poll.\n" +
							"Votes: "+status, participant)
					);
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

	@Override
	public void processAddVoteOption(AddVoteOptionRequest request) {
		if (!isInitializedSessionOnRequestChat(request)) return;
		
		VotingSession votingSession = manager.getSessionForRequest(request);
		ChatAdapterInterface chat = request.getChat();
		boolean added = votingSession.addOption(request.getName());
		if (!added) {
			onReplyPrivate(chat, "Option '"+request.getName()+"' already added.");
			return;
		}
		String header = String.format("New option '%s' added by %s. Current options:\n", 
				request.getName(),chat.getLasterSenderFullName());
		
		String reply =
				header+
				buildVotingMenu(chat, votingSession).trim();
		onReply(chat, reply);
	}

	@Override
	public void processMissingVoteRequest(MissingVotersRequest request) {
		VotingSession votingSession = manager.getSessionForRequest(request);
		final StringBuilder sb = new StringBuilder();
		votingSession.acceptParticipantConsultant(new ParticipantConsultant() {

			@Override
			public void visit(String participantName, boolean hasVoted) {
				if (!hasVoted)
					sb.append(participantName+", ");
			}
		});
		String withoutTrailingCommand = StringUtils.substring(sb.toString(),0,-2);
		String reply = "Users that haven't voted yet:\n\t"+withoutTrailingCommand;
		onReply(request.getChat(), reply);
	}
}