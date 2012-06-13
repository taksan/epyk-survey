package skype.voting.requests;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandHelper;

public class HelpRequest extends AbstractShellCommand implements ShellCommand {

	private final ShellCommandHelper[] factories;
	
	public HelpRequest(ChatAdapterInterface chat, String command, ShellCommandHelper... factories) {
		super(chat, command);
		this.factories = factories;
	}
	
	HelpRequest(ShellCommandHelper... factories) {
		this(null,null,factories);
	}

	public String getHelpMessage() {
		StringBuilder help = new StringBuilder();
		for (ShellCommandHelper helper : factories) {
			help.append("Command: " + helper.getHelp()+"\n");
		}
		return "Available commands:\n"+help.toString().trim(); 
	}
}
