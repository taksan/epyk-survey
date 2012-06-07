package skype.voting;

import skype.voting.requests.VoteRequest;
import skype.voting.requests.VotingPollRequest;

public interface VotingSession {

	public abstract void initWith(VotingPollRequest request);

	public abstract void vote(VoteRequest voteRequest);

	public abstract void acceptVoteConsultant(VotingConsultant consultant);

	public abstract void acceptWinnerCheckerVisitor(WinnerConsultant consultant);

}