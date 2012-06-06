package skype.lunch;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

public class VotingSession implements LunchRequestVisitor {
	Vector<LunchOption> optionByIndex = null;
	Map<String, LunchOption> participants = new LinkedHashMap<String, LunchOption>();
	Map<LunchOption, Integer> voteStatus = new LinkedHashMap<LunchOption, Integer>();
	
	public void initWith(LunchRequest request) {
		optionByIndex = new Vector<LunchOption>();
		participants = new LinkedHashMap<String, LunchOption>();
		voteStatus = new LinkedHashMap<LunchOption, Integer>();
		
		request.accept(this);
	}
	
	@Override
	public void visitOption(LunchOption option) {
		voteStatus.put(option, 0);
		optionByIndex.add(option);
	}

	@Override
	public void visitParticipant(String participantName) {
		participants.put(participantName, null);
	}


	public void vote(VoteRequest voteRequest) {
		if (optionByIndex == null)
			return;
		LunchOption lunchOption = optionByIndex.get(voteRequest.vote-1);
		participants.put(voteRequest.sender, lunchOption);
	}
	
	public void acceptVoteConsultant(VotingConsultant consultant){
		Map<LunchOption, Integer> updatedVotingStatus = getUpdatedVotingStatus();
		for (Entry<LunchOption, Integer> optionVoteCount: updatedVotingStatus.entrySet()) {
			consultant.onVote(optionVoteCount.getKey(), optionVoteCount.getValue());
		}
	}
	
	String getParticipants() {
		return StringUtils.join(participants.keySet(),",");
	}
	
	String getVoteOptions() {
		return StringUtils.join(optionByIndex,",");
	}

	String getVotingTable() {
		final StringBuilder sb = new StringBuilder();
		acceptVoteConsultant(new VotingConsultant() {
			@Override
			public void onVote(LunchOption option, Integer count) {
				sb.append(option.getName()+":"+count+"\n");
			}
		});
		
		return sb.toString().trim();
	}

	private Map<LunchOption, Integer> getUpdatedVotingStatus() {
		for (LunchOption participantVote : participants.values()) {
			if (participantVote == null)
				continue;
			Integer voteCount = voteStatus.get(participantVote);
			voteStatus.put(participantVote, voteCount+1);
		}
		return voteStatus;
	}

}
