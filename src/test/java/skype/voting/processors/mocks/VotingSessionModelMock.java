package skype.voting.processors.mocks;

import skype.shell.ShellCommand;
import skype.voting.VotingSessionModel;
import skype.voting.application.ParticipantConsultant;
import skype.voting.application.VoteFeedbackHandler;
import skype.voting.application.VotingConsultant;
import skype.voting.application.VotingSession;
import skype.voting.application.WinnerConsultant;
import skype.voting.processors.requests.ClosePollRequest;
import skype.voting.processors.requests.StartPollRequest;
import skype.voting.processors.requests.VoteRequest;
import skype.voting.processors.requests.VotingPollVisitor;

public class VotingSessionModelMock implements VotingSessionModel {
	StringBuilder sb = new StringBuilder();

	@Override
	public VotingSession getSessionForRequest(ShellCommand request) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public VotingSession makeNewVotingSession(StartPollRequest votePollRequest) {
		return new VotingSession() {
			
			@Override
			public void vote(VoteRequest voteRequest, VoteFeedbackHandler handler) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void vote(VoteRequest voteRequest) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void removeParticipant(String participant) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void initWith(StartPollRequest request) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public boolean addOption(String name) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void addNewParticipant(String participant) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void acceptWinnerConsultant(WinnerConsultant consultant) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void acceptVoteConsultant(VotingConsultant consultant) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void acceptParticipantConsultant(ParticipantConsultant consultant) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public void accept(VotingPollVisitor votingPollVisitor) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
		};
	}

	@Override
	public void removeSessionForGivenRequest(ClosePollRequest request) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public boolean isInitializedSessionOnRequestChat(ShellCommand request) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

}
