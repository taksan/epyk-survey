package skype.voting;

import skype.voting.application.VotingConsultant;
import skype.voting.application.VotingPollOption;

public class VotingStatusMessageFormatter implements VotingConsultant {
	final StringBuilder sb = new StringBuilder();
	@Override
	public void onVote(VotingPollOption option, Integer count) {
		sb.append(option+": " + count);
		sb.append(" ; ");
	}

	public String getFormattedStatus() {
		return sb.toString().replaceAll(" ; $", "");
	}
}
