package skype.voting.application;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import skype.voting.processors.requests.StartPollRequest;
import skype.voting.processors.requests.VoteRequest;
import skype.voting.processors.requests.VotingPollVisitor;

public class VotingSessionImpl implements VotingPollVisitor, VotingSession {
	Vector<VotingPollOption> voteOptionByIndex = null;
	Map<String, VotingPollOption> participantsAndVotes = new LinkedHashMap<String, VotingPollOption>();
	private String welcome;
	
	@Override
	public void initWith(StartPollRequest request) {
		voteOptionByIndex = new Vector<VotingPollOption>();
		participantsAndVotes = new LinkedHashMap<String, VotingPollOption>();
		
		request.accept(this);
	}

	@Override
	public void vote(VoteRequest voteRequest, VoteFeedbackHandler handler) {
		if (isVotingSessionStarted())
			return;
		int index = voteRequest.vote-1;
		if (index >= voteOptionByIndex.size() || index < 0) {
			handler.handleError("Invalid voting option " + voteRequest.vote);
			return;
		}
		VotingPollOption lunchOption = voteOptionByIndex.get(index);
		participantsAndVotes.put(voteRequest.sender, lunchOption);
		handler.handleSuccess();
	}
	
	@Override
	public void vote(VoteRequest voteRequest) {
		vote(voteRequest, new ThrowsFeedbackHandler());
	}

	@Override
	public void acceptVoteConsultant(VotingConsultant consultant){
		Map<VotingPollOption, Integer> updatedVotingStatus = getUpdatedVotingStatus();
		for (Entry<VotingPollOption, Integer> optionVoteCount: updatedVotingStatus.entrySet()) {
			consultant.onVote(optionVoteCount.getKey(), optionVoteCount.getValue());
		}
	}

	@Override
	public void acceptWinnerConsultant(WinnerConsultant consultant) {
		Map<VotingPollOption, Integer> status = getUpdatedVotingStatus();
		if (thereAreNoVotes(status))
			return;
		
		VotingPollOption top = status.keySet().iterator().next();
		Set<VotingPollOption> tiedOptions = new LinkedHashSet<VotingPollOption>();
		int bestCount = 0;
		for (Entry<VotingPollOption, Integer> voteCount : status.entrySet()) {
			VotingPollOption currentOption = voteCount.getKey();
			if (voteCount.getValue() > bestCount) {
				bestCount = voteCount.getValue();
				top = currentOption;
				tiedOptions.clear();
			}
			if (voteCount.getValue() == bestCount) {
				tiedOptions.add(currentOption);
			}
		}
		tiedOptions.add(top);
		if (tiedOptions.size() > 1)
			consultant.onTie(tiedOptions, bestCount);
		else
			consultant.onWinner(new VoteOptionAndCount(top.getName(), bestCount));
	}

	@Override
	public void visitOption(VotingPollOption option) {
		voteOptionByIndex.add(option);
	}

	@Override
	public void visitParticipant(String participantName) {
		addNewParticipant(participantName);
	}
	
	@Override
	public void onWelcomeMessage(String message) {
		welcome = message;
	}
	
	@Override
	public void addNewParticipant(String participant) {
		participantsAndVotes.put(participant, null);
	}

	@Override
	public void removeParticipant(String participant) {
		participantsAndVotes.remove(participant);
	}	

	String getParticipants() {
		return StringUtils.join(participantsAndVotes.keySet(),",");
	}
	
	String getVoteOptions() {
		return StringUtils.join(voteOptionByIndex,",");
	}

	private boolean thereAreNoVotes(Map<VotingPollOption, Integer> status) {
		return status.entrySet().size() == 0;
	}

	private boolean isVotingSessionStarted() {
		return voteOptionByIndex == null;
	}

	private Map<VotingPollOption, Integer> getUpdatedVotingStatus() {
		Map<VotingPollOption, Integer> voteStatus = new LinkedHashMap<VotingPollOption, Integer>();
		if (isVotingSessionStarted())
			return voteStatus;
		initializeAllOptionVotesToZero(voteStatus);
		for (VotingPollOption participantVote : participantsAndVotes.values()) {
			if (participantVote == null)
				continue;
			Integer voteCount = voteStatus.get(participantVote);
			if (voteCount == null)
				voteCount = 0;
			voteStatus.put(participantVote, voteCount+1);
		}
		return voteStatus;
	}

	private void initializeAllOptionVotesToZero(Map<VotingPollOption, Integer> voteStatus) {
		for (VotingPollOption option : voteOptionByIndex) {
			voteStatus.put(option, 0);
		}
	}

	@Override
	public boolean addOption(String name) {
		for (VotingPollOption e : voteOptionByIndex) {
			if (e.getName().trim().equalsIgnoreCase(name.trim()))
				return false;
		}
		VotingPollOption option = new VotingPollOption(name.trim());
		voteOptionByIndex.add(option);
		return true;
	}

	@Override
	public void accept(VotingPollVisitor visitor) {
		visitor.onWelcomeMessage(welcome);
		for (VotingPollOption e : voteOptionByIndex) {
			visitor.visitOption(e);
		}
		for (String p : participantsAndVotes.keySet()) {
			visitor.visitParticipant(p);
		}
	}

	@Override
	public void acceptParticipantConsultant(ParticipantConsultant consultant) {
		for (Entry<String, VotingPollOption> vote : participantsAndVotes.entrySet()) {
			boolean hasVoted = vote.getValue()!=null;
			consultant.visit(vote.getKey(), hasVoted);
		}
	}

	
}
