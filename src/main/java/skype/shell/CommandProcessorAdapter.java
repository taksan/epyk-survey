package skype.shell;

import skype.voting.requests.AddVoteOptionRequest;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VoteStatusRequest;
import skype.voting.requests.StartPollRequest;

public abstract class CommandProcessorAdapter implements CommandProcessor {

	@Override
	public void addReplyListener(ReplyListener listener) {
		// do nothing
	}

	@Override
	public void processVotingPollRequest(StartPollRequest lunchRequest) {
		// do nothing
	}

	@Override
	public void processVoteRequest(VoteRequest request) {
		// do nothing
	}

	@Override
	public void processClosePollRequest(ClosePollRequest closePollRequest) {
		// do nothing
	}

	@Override
	public void processUnrecognizedCommand(UnrecognizedCommand visitor) {
		// do nothing
	}

	@Override
	public void processVoteStatusRequest(VoteStatusRequest voteStatusRequest) {
		// do nothing
	}
	
	@Override
	public void processAddVoteOption(AddVoteOptionRequest addVoteOptionRequest) {
		// do nothing
	}
	
	@Override
	public void processHelpCommand(HelpRequest helpRequest) {
		// do nothing
	}
}
