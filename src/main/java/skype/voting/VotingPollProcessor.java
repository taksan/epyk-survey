package skype.voting;

import skype.shell.CommandProcessor;
import skype.shell.ReplyListener;
import skype.shell.UnrecognizedCommand;

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
	public void processLunchRequest(VotingPollRequest lunchRequest) {
		votingSession = votingSessionFactory.produce();
		votingSession.initWith(lunchRequest);
		
		String reply = buildVotingMenu(lunchRequest);
		listener.onReply(lunchRequest.getChat(), reply);
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
	public void processVoteRequest(VoteRequest request) {
		votingSession.vote(request);
		
		final StringBuilder sb = new StringBuilder();
		votingSession.acceptVoteConsultant(new VotingConsultant() {
			public void onVote(VotingPollOption option, Integer count) {
				sb.append(option+": " + count);
				sb.append(" ; ");
			}
		});
		
		String voteStatus = sb.toString().replaceAll(" ; $", "");
		if (voteStatus.isEmpty())
			return;
		String reply = "Votes: " + voteStatus;
		listener.onReply(request.getChat(), reply);
	}
	
	@Override
	public void addReplyListener(ReplyListener listener) {
		this.listener = listener;
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
