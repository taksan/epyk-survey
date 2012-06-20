package skype.voting.processors.requests;

import java.util.LinkedHashSet;
import java.util.Set;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.voting.application.VotingPollOption;


public class StartPollRequest extends AbstractShellCommand {
	private Set<VotingPollOption> lunchOptions = new LinkedHashSet<VotingPollOption>();
	private Set<String> participants = new LinkedHashSet<String>();
	private String welcome;
	public StartPollRequest(ChatAdapterInterface chat, String command) {
		super(chat,command);
	}
	
	public StartPollRequest(ChatAdapterInterface chat) {
		super(chat, "auto");
	}
	
	public void add(VotingPollOption anOption) {
		lunchOptions.add(anOption);
	}

	public void accept(VotingPollVisitor visitor){
		visitor.onWelcomeMessage(welcome);
		for (VotingPollOption anOption : lunchOptions) {
			visitor.visitOption(anOption);
		}
		for (String participantName : participants) {
			visitor.visitParticipant(participantName);
		}
	}

	public int getOptionCount() {
		return lunchOptions.size();
	}

	public void addParticipant(String participantName) {
		participants.add(participantName);
	}

	public void setWelcomeMessage(String welcome) {
		this.welcome = welcome;
	}
}
