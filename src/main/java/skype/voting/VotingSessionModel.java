package skype.voting;

import skype.shell.ShellCommand;
import skype.voting.application.VotingSession;
import skype.voting.processors.requests.ClosePollRequest;
import skype.voting.processors.requests.StartPollRequest;

public interface VotingSessionModel {

	public abstract VotingSession getSessionForRequest(ShellCommand request);

	public abstract VotingSession makeNewVotingSession(StartPollRequest votePollRequest);

	public abstract void removeSessionForGivenRequest(final ClosePollRequest request);

	public abstract boolean isInitializedSessionOnRequestChat(ShellCommand request);

}