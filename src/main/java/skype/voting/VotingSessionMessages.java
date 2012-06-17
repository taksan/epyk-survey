package skype.voting;

import org.apache.commons.lang.StringUtils;

import skype.voting.application.VotingPollOption;
import skype.voting.application.VotingSession;
import skype.voting.requests.VotingPollVisitor;

public class VotingSessionMessages implements VotingSessionMessageInterface {

	/* (non-Javadoc)
	 * @see skype.voting.VotingSessionMessageProvider#getVotingStatusMessage(skype.voting.application.VotingSession)
	 */
	@Override
	public  String getVotingStatusMessage(VotingSession votingSession) {
		VotingStatusMessageFormatter formatter = new VotingStatusMessageFormatter();
		votingSession.acceptVoteConsultant(formatter);
		return formatter.getFormattedStatus();
	}

	/* (non-Javadoc)
	 * @see skype.voting.VotingSessionMessageProvider#buildVotingMenu(skype.voting.application.VotingSession)
	 */
	@Override
	public  String buildVotingMenu(VotingSession session) {
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

	/* (non-Javadoc)
	 * @see skype.voting.VotingSessionMessageProvider#buildVotingMenuWithoutVoters(skype.voting.application.VotingSession)
	 */
	@Override
	public  String buildVotingMenuWithoutVoters(VotingSession targetSession) {
		String buildGuidelineText = buildVotingMenu(targetSession);
		return buildGuidelineText.replaceAll("Voters:.*", "");
	}

	/* (non-Javadoc)
	 * @see skype.voting.VotingSessionMessageProvider#getUpdatedVotingMenu(skype.voting.application.VotingSession)
	 */
	@Override
	public  String getUpdatedVotingMenu(VotingSession session) {
		String reply = buildVotingMenu(session);
		return reply;
	}

}
