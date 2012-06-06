package skype.lunch;

import java.util.LinkedHashSet;
import java.util.Set;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandProcessor;


public class LunchRequest extends AbstractShellCommand {
	private Set<LunchOption> lunchOptions = new LinkedHashSet<LunchOption>();
	private Set<String> participants = new LinkedHashSet<String>();
	LunchRequest(ChatAdapterInterface chat, String command) {
		super(chat,command);
	}
	
	LunchRequest(ChatAdapterInterface chat) {
		super(chat, "auto");
	}
	
	public void add(LunchOption anOption) {
		lunchOptions.add(anOption);
	}

	public void accept(LunchRequestVisitor visitor){
		for (LunchOption anOption : lunchOptions) {
			visitor.visitOption(anOption);
		}
		for (String participantName : participants) {
			visitor.visitParticipant(participantName);
		}
	}

	@Override
	public void acceptProcessorForSentMessages(CommandProcessor processor) {
		processor.processLunchRequest(this);
	}

	@Override
	public void acceptProcessorForReceivedMessages(CommandProcessor processor) {
		processor.processLunchRequest(this);
	}

	public int getOptionCount() {
		return lunchOptions.size();
	}

	public void addParticipant(String participantName) {
		participants.add(participantName);
	}
}
