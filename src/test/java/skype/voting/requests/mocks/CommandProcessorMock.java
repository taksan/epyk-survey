package skype.voting.requests.mocks;

import skype.shell.CommandProcessor;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.shell.UnrecognizedCommand;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VotingPollRequest;

public class CommandProcessorMock implements CommandProcessor {
	public ShellCommand receivedRequest = null;

	@Override
	public void processVotingPollRequest(VotingPollRequest request) {
		receivedRequest = request;
	}

	@Override
	public void processVoteRequest(VoteRequest request) {
		receivedRequest = request;
	}

	@Override
	public void processClosePollRequest(ClosePollRequest request) {
		receivedRequest = request;
	}

	@Override
	public void processUnrecognizedCommand(UnrecognizedCommand request) {
		receivedRequest = request;
	}

	@Override
	public void addReplyListener(ReplyListener listener) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

}
