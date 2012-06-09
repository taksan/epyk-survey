package skype.voting.mocks;

import java.util.LinkedHashSet;

import skype.voting.VoteOptionAndCount;
import skype.voting.VotingConsultant;
import skype.voting.VotingPollOption;
import skype.voting.VotingPollVisitor;
import skype.voting.VotingSession;
import skype.voting.WinnerConsultant;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VotingPollRequest;

public class VotingSessionMockAdapter implements VotingSession {
	public VotingPollRequest pollRequest;
	private final boolean isTie;
	private VotingPollOption fooOption = new VotingPollOption("foo");
	private VotingPollOption bazOption = new VotingPollOption("baz");
	private String participants="";

	public VotingSessionMockAdapter(boolean isTie) {
		this.isTie = isTie;
	}

	@Override
	public void initWith(VotingPollRequest request) {
		pollRequest = request;
		request.accept(new VotingPollVisitor() {
			@Override
			public void visitParticipant(String participantName) {
				addNewParticipant(participantName);
			}
			
			@Override
			public void visitOption(VotingPollOption option) {
			}
			
			@Override
			public void onWelcomeMessage(String message) {
			}
		});
	}
	
	@Override
	public void acceptVoteConsultant(VotingConsultant consultant) {
		if (pollRequest != null) {
			consultant.onVote(fooOption, getFooCount());
			consultant.onVote(bazOption, 3);
		}
	}

	private Integer getFooCount() {
		if (isTie)
			return 3;
		return 2;
	}

	@Override
	public void vote(VoteRequest voteRequest) {
		//
	}

	@Override
	public void acceptWinnerConsultant(WinnerConsultant consultant) {
		if (pollRequest != null) {
			if (isTie) {
				LinkedHashSet<VotingPollOption> tied = new LinkedHashSet<VotingPollOption>();
				tied.add(fooOption);
				tied.add(bazOption);
				consultant.onTie(tied, 3);
			}
			else
				consultant.onWinner(new VoteOptionAndCount("baz", 3));
		}
	}

	@Override
	public void addNewParticipant(String participant) {
		String comma=",";
		if (participants.isEmpty())
			comma="";
		participants+=comma+participant;
	}

	@Override
	public void removeParticipant(String participant) {
		participants = participants.replace(participant, "").
				replaceAll(",,",",").replaceFirst("^,", "");
	}

	public String getParticipants() {
		return participants;
	}
}
