package skype.voting.application;

public class VoteOptionAndCount {

	public final String optionName;
	public final Integer voteCount;

	public VoteOptionAndCount(String optionName, Integer voteCount) {
		this.optionName = optionName;
		this.voteCount = voteCount;
	}

	@Override
	public String toString() {
		return "VoteOptionAndCount [optionName=" + optionName + ", voteCount=" + voteCount + "]";
	}
}
