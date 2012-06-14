package skype.voting.application;

import skype.voting.requests.VoteRequest;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.VotingPollVisitor;

public interface VotingSession {

	public abstract void initWith(StartPollRequest request);

	public abstract void vote(VoteRequest voteRequest);

	public abstract void addNewParticipant(String participant);
	
	public abstract void removeParticipant(String participant);
	
	public abstract void acceptVoteConsultant(VotingConsultant consultant);

	public abstract void accept(VotingPollVisitor votingPollVisitor);
	
	public abstract void acceptWinnerConsultant(WinnerConsultant consultant);

	public abstract boolean addOption(String name);

	public abstract void acceptParticipantConsultant(ParticipantConsultant consultant);

	public abstract void vote(VoteRequest voteRequest, VoteFeedbackHandler handler);

}