package skype.shell;

import skype.voting.VotingPollRequest;
import skype.voting.VoteRequest;

public interface CommandProcessor {
	void processLunchRequest(VotingPollRequest lunchRequest);
	void processUnrecognizedCommand(UnrecognizedCommand visitor);
	void addReplyListener(ReplyListener listener);
	void processVoteRequest(VoteRequest request);
}
