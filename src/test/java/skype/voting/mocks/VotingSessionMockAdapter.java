package skype.voting.mocks;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import skype.voting.VoteOptionAndCount;
import skype.voting.VotingConsultant;
import skype.voting.VotingPollOption;
import skype.voting.VotingSession;
import skype.voting.WinnerConsultant;
import skype.voting.requests.StartPollRequest;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.VotingPollVisitor;

public class VotingSessionMockAdapter implements VotingSession {
	public StartPollRequest pollRequest;
	private final boolean isTie;
	private VotingPollOption fooOption = new VotingPollOption("foo");
	private VotingPollOption bazOption = new VotingPollOption("baz");
	private List<VotingPollOption> voteOptions= new ArrayList<VotingPollOption>();

	public VotingSessionMockAdapter(boolean isTie) {
		this.isTie = isTie;
		voteOptions.add(fooOption);
		voteOptions.add(bazOption);
	}

	private String welcome;
	@Override
	public void initWith(StartPollRequest request) {
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
				welcome = message;
			}
		});
	}
	
	@Override
	public void acceptVoteConsultant(VotingConsultant consultant) {
		if (pollRequest != null) {
			for (VotingPollOption e : voteOptions) {
				consultant.onVote(e, getCountFor(e));
			}
		}
	}

	private Integer getCountFor(VotingPollOption e) {
		if (e.equals(fooOption))
			return getFooCount();
		if (e.equals(bazOption))
			return 3;
		return 0;
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

	List<String> participants = new ArrayList<String>();
	@Override
	public void addNewParticipant(String participant) {
		participants.add(participant);
	}

	@Override
	public void removeParticipant(String participant) {
		participants.remove(participant);
	}

	public String getParticipants() {
		return StringUtils.join(participants,",");
	}

	@Override
	public boolean addOption(String name) {
		String join = StringUtils.join(voteOptions,",");
		if (join.contains(name))
			return false;
		voteOptions.add(new VotingPollOption(name));
		return true;
	}

	@Override
	public void accept(VotingPollVisitor visitor) {
		visitor.onWelcomeMessage(welcome);
		for (VotingPollOption anOption : voteOptions) {
			visitor.visitOption(anOption);
		}
		for (String participantName : participants) {
			visitor.visitParticipant(participantName);
		}
	}
}
