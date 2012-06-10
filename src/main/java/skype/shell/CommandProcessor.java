package skype.shell;

import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VoteStatusRequest;
import skype.voting.requests.StartPollRequest;

public interface CommandProcessor {
	void addReplyListener(ReplyListener listener);
	void processVotingPollRequest(StartPollRequest lunchRequest);
	void processVoteRequest(VoteRequest request);
	void processClosePollRequest(ClosePollRequest closePollRequest);
	void processUnrecognizedCommand(UnrecognizedCommand visitor);
	void processVoteStatusRequest(VoteStatusRequest voteStatusRequest);
	void processHelpCommand(HelpRequest helpRequest);
}
