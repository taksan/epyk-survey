package skype.voting;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

public class VotingSessionImpl implements VotingPollVisitor, VotingSession {
	Vector<VotingPollOption> optionByIndex = null;
	Map<String, VotingPollOption> participants = new LinkedHashMap<String, VotingPollOption>();
	Map<VotingPollOption, Integer> voteStatus = new LinkedHashMap<VotingPollOption, Integer>();
	
	/* (non-Javadoc)
	 * @see skype.voting.VotingSessionInterface#initWith(skype.voting.VotingPollRequest)
	 */
	@Override
	public void initWith(VotingPollRequest request) {
		optionByIndex = new Vector<VotingPollOption>();
		participants = new LinkedHashMap<String, VotingPollOption>();
		voteStatus = new LinkedHashMap<VotingPollOption, Integer>();
		
		request.accept(this);
	}

	/* (non-Javadoc)
	 * @see skype.voting.VotingSessionInterface#vote(skype.voting.VoteRequest)
	 */
	@Override
	public void vote(VoteRequest voteRequest) {
		if (optionByIndex == null)
			return;
		VotingPollOption lunchOption = optionByIndex.get(voteRequest.vote-1);
		participants.put(voteRequest.sender, lunchOption);
	}
	
	/* (non-Javadoc)
	 * @see skype.voting.VotingSessionInterface#acceptVoteConsultant(skype.voting.VotingConsultant)
	 */
	@Override
	public void acceptVoteConsultant(VotingConsultant consultant){
		Map<VotingPollOption, Integer> updatedVotingStatus = getUpdatedVotingStatus();
		for (Entry<VotingPollOption, Integer> optionVoteCount: updatedVotingStatus.entrySet()) {
			consultant.onVote(optionVoteCount.getKey(), optionVoteCount.getValue());
		}
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
