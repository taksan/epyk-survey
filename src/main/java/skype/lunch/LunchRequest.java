package skype.lunch;

import java.util.LinkedHashSet;
import java.util.Set;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandProcessor;


public class LunchRequest extends AbstractShellCommand {
	private Set<LunchOption> lunchOptions = new LinkedHashSet<LunchOption>();
	LunchRequest(ChatAdapterInterface chat, String command) {
		super(chat,command);
		buildRequest(command);
	}
	
	LunchRequest(ChatAdapterInterface chat) {
		super(chat, "auto");
	}
	
	public void add(LunchOption anOption) {
		lunchOptions.add(anOption);
	}

	private void buildRequest(String command) {
		command = command.replaceAll("#lunch[ ]*","");
		String[] optionNames = command.split(",");
		for (String aPlace : optionNames) {
			add(new LunchOption(aPlace));
		}
	}


	public void accept(LunchRequestVisitor visitor){
		for (LunchOption anOption : lunchOptions) {
			visitor.visit(anOption);
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
}
