package skype.voting;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

import skype.voting.requests.VoteRequest;
import skype.voting.requests.VotingPollRequest;

public class VotingSessionImpl implements VotingPollVisitor, VotingSession {
	Vector<VotingPollOption> optionByIndex = null;
	Map<String, VotingPollOption> participants = new LinkedHashMap<String, VotingPollOption>();
	Map<VotingPollOption, Integer> voteStatus = new LinkedHashMap<VotingPollOption, Integer>();
	
	@Override
	public void initWith(VotingPollRequest request) {
		optionByIndex = new Vector<VotingPollOption>();
		participants = new LinkedHashMap<String, VotingPollOption>();
		voteStatus = new LinkedHashMap<VotingPollOption, Integer>();
		
		request.accept(this);
	}

	@Override
	public void vote(VoteRequest voteRequest) {
		if (optionByIndex == null)
			return;
		VotingPollOption lunchOption = optionByIndex.get(voteRequest.vote-1);
		participants.put(voteRequest.sender, lunchOption);
	}
	
	@Override
	public void acceptVoteConsultant(VotingConsultant consultant){
		Map<VotingPollOption, Integer> updatedVotingStatus = getUpdatedVotingStatus();
		for (Entry<VotingPollOption, Integer> optionVoteCount: updatedVotingStatus.entrySet()) {
			consultant.onVote(optionVoteCount.getKey(), optionVoteCount.getValue());
		}
	}

	@Override
	public void acceptWinnerCheckerVisitor(WinnerConsultant consultant) {
		Map<VotingPollOption, Integer> status = getUpdatedVotingStatus();
		if (status.entrySet().size() == 0)
			return;
		
		VotingPollOption top = status.keySet().iterator().next();
		Set<VotingPollOption> tied = new LinkedHashSet<VotingPollOption>();
		int bestCount = 0;
		for (Entry<VotingPollOption, Integer> voteCount : status.entrySet()) {
			VotingPollOption currentOption = voteCount.getKey();
			if (voteCount.getValue() > bestCount) {
				bestCount = voteCount.getValue();
				top = currentOption;
				tied.clear();
			}
			if (voteCount.getValue() == bestCount) {
				tied.add(currentOption);
			}
		}
		tied.add(top);
		if (tied.size() > 1)
			consultant.onTie(tied, bestCount);
		else
			consultant.onWinner(new VoteOptionAndCount(top.getName(), bestCount));
	}
		
	
	@Override
	public void visitOption(VotingPollOption option) {
		voteStatus.put(option, 0);
		optionByIndex.add(option);
	}

	@Override
	public void visitParticipant(String participantName) {
		participants.put(participantName, null);
	}

	String getParticipants() {
		return StringUtils.join(participants.keySet(),",");
	}
	
	String getVoteOptions() {
		return StringUtils.join(optionByIndex,",");
	}

	private Map<VotingPollOption, Integer> getUpdatedVotingStatus() {
		for (VotingPollOption participantVote : participants.values()) {
			if (participantVote == null)
				continue;
			Integer voteCount = voteStatus.get(participantVote);
			voteStatus.put(participantVote, voteCount+1);
		}
		return voteStatus;
	}


}
