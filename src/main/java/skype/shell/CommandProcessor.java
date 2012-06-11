package skype.shell;

import skype.voting.requests.AddVoteOptionRequest;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.MissingVotersRequest;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VoteStatusRequest;

public interface CommandProcessor {
	void addReplyListener(ReplyListener listener);
	void processVotingPollRequest(StartPollRequest lunchRequest);
	void processVoteRequest(VoteRequest request);
	void processClosePollRequest(ClosePollRequest closePollRequest);
	void processUnrecognizedCommand(UnrecognizedCommand visitor);
	void processVoteStatusRequest(VoteStatusRequest voteStatusRequest);
	void processHelpCommand(HelpRequest helpRequest);
	void processAddVoteOption(AddVoteOptionRequest addVoteOptionRequest);
	void processMissingVoteRequest(MissingVotersRequest missingVotersRequest);
	void processReplyTextRequest(ReplyTextRequest replyTextRequest);
}
