package skype.voting;

import java.util.Set;

import skype.shell.CommandProcessor;
import skype.shell.ReplyListener;
import skype.shell.UnrecognizedCommand;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VotingPollRequest;

public class VotingPollProcessor implements CommandProcessor {

	private ReplyListener listener;
	VotingSession votingSession = new VotingSessionImpl();
	private final VotingSessionFactory votingSessionFactory;
	
	public VotingPollProcessor() {
		this(new VotingSessionFactoryImpl());
	}
	
	VotingPollProcessor(VotingSessionFactory votingSessionFactory){
		this.votingSessionFactory = votingSessionFactory;
		votingSession = votingSessionFactory.produce();
	}

	@Override
	public void processLunchRequest(VotingPollRequest votePollRequest) {
		votingSession = votingSessionFactory.produce();
		votingSession.initWith(votePollRequest);
		
		String reply = buildVotingMenu(votePollRequest);
		listener.onReply(votePollRequest.getChat(), reply);
	}

	@Override
	public void processVoteRequest(VoteRequest request) {
		votingSession.vote(request);
		
		String voteStatus = getVotingStatusMessage();
		if (voteStatus.isEmpty())
			return;
		
		String reply = "Votes: " + voteStatus;
		listener.onReply(request.getChat(), reply);
	}
	
	@Override
	public void processClosePollRequest(final ClosePollRequest request) {
		votingSession.acceptWinnerCheckerVisitor(new WinnerConsultant() {
			@Override
			public void onWinner(VoteOptionAndCount winnerStats) {
				String voteStatus = getVotingStatusMessage();
				String winnerMessage = 
						String.format("WINNER: ***%s*** with %d vote%s",
								winnerStats.optionName,
								winnerStats.voteCount,
								getPlural(winnerStats.voteCount)
								);
				String reply = "Votes: " + voteStatus + "\n" +	winnerMessage;
				votingSession = votingSessionFactory.produce();
				listener.onReply(request.getChat(), reply);
			}

			@Override
			public void onTie(Set<VotingPollOption> tiedOptions, int tieCount) {
				String voteStatus = getVotingStatusMessage();
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
				
				votingSession = votingSessionFactory.produce();
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
	
	private String getVotingStatusMessage() {
		final StringBuilder sb = new StringBuilder();
		votingSession.acceptVoteConsultant(new VotingConsultant() {
			public void onVote(VotingPollOption option, Integer count) {
				sb.append(option+": " + count);
				sb.append(" ; ");
			}
		});
		
		String voteStatus = sb.toString().replaceAll(" ; $", "");
		return voteStatus;
	}

	private String buildVotingMenu(VotingPollRequest lunchRequest) {
		final StringBuffer msg = new StringBuffer();
		msg.append("Almoço!\n");
		lunchRequest.accept(new VotingPollVisitor() {
			int count=1;
			@Override
			public void visitOption(VotingPollOption option) {
				msg.append(count+") ");
				msg.append(option.getName());
				msg.append("\n");
				count++;
			}
			@Override
			public void visitParticipant(String participantName) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
		});
		String reply = "\n"+msg.toString().trim();
		return reply;
	}

}
