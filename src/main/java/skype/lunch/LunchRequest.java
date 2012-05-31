package skype.lunch;

import java.util.LinkedHashSet;
import java.util.Set;

import skype.ChatId;
import skype.shell.AbstractShellCommand;
import skype.shell.ShellCommandVisitor;


public class LunchRequest extends AbstractShellCommand {
	private Set<LunchOption> lunchOptions = new LinkedHashSet<LunchOption>();
	public LunchRequest(ChatId chatId, String command) {
		super(chatId,command);
		buildRequest(command);
	}
	
	LunchRequest() {
		super(new ChatId("autoid"), "auto");
	}
	
	void add(LunchOption anOption) {
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
	public void acceptSentRequest(ShellCommandVisitor visitor) {
		visitor.processSentRequest(this);
	}
}
