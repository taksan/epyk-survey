package skype.shell;

import skype.lunch.LunchRequest;
import skype.lunch.VoteRequest;

public interface CommandProcessor {
	void processLunchRequest(LunchRequest lunchRequest);
	void processUnrecognizedCommand(UnrecognizedCommand visitor);
	void addReplyListener(ReplyListener listener);
	void processVoteRequest(VoteRequest request);
}
