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
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void processVotingPollRequest(StartPollRequest lunchRequest) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void processVoteRequest(VoteRequest request) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void processClosePollRequest(ClosePollRequest closePollRequest) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void processUnrecognizedCommand(UnrecognizedCommand visitor) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void processVoteStatusRequest(VoteStatusRequest voteStatusRequest) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
	
	@Override
	public void processHelpCommand(HelpRequest helpRequest) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void processAddVoteOption(AddVoteOptionRequest addVoteOptionRequest) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
}
