package skype.shell;

import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VotingPollRequest;

public interface CommandProcessor {
	void processVotingPollRequest(VotingPollRequest lunchRequest);
	void processVoteRequest(VoteRequest request);
	void processClosePollRequest(ClosePollRequest closePollRequest);
	void processUnrecognizedCommand(UnrecognizedCommand visitor);
	void addReplyListener(ReplyListener listener);
}
