package skype.lunch;

import java.util.LinkedHashMap;
import java.util.Map;

import skype.shell.CommandProcessor;
import skype.shell.ReplyListener;
import skype.shell.UnrecognizedCommand;

public class LunchProcessor implements CommandProcessor {

	private ReplyListener listener;
	VotingSession votingSession = new VotingSession();

	@Override
	public void processLunchRequest(LunchRequest lunchRequest) {
		votingSession.initWith(lunchRequest);
		
		String reply = buildVotingMenu(lunchRequest);
		listener.onReply(lunchRequest.getChat(), reply);
	}

	private String buildVotingMenu(LunchRequest lunchRequest) {
		final StringBuffer msg = new StringBuffer();
		msg.append("Almoço!\n");
		lunchRequest.accept(new LunchRequestVisitor() {
			int count=1;
			@Override
			public void visitOption(LunchOption option) {
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

	Map<String, Integer> votesByOption = new LinkedHashMap<String, Integer>();
	@Override
	public void processVoteRequest(VoteRequest request) {
		votingSession.vote(request);
		
		final StringBuilder sb = new StringBuilder();
		votingSession.acceptVoteConsultant(new VotingConsultant() {
			public void onVote(LunchOption option, Integer count) {
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
}
