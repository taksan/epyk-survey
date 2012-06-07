package skype.voting;

public interface VotingSession {

	public abstract void initWith(VotingPollRequest request);

	public abstract void vote(VoteRequest voteRequest);

	public abstract void acceptVoteConsultant(VotingConsultant consultant);

}