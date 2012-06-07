package skype.voting;

import java.util.Set;

public interface WinnerConsultant {
	public void onWinner(VoteOptionAndCount winnerStats);
	public void onTie(Set<VotingPollOption> tiedOptions, int tieCount);
}
