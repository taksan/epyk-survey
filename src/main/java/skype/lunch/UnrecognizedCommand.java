package skype.lunch;

import skype.shell.ShellCommand;
import skype.shell.ShellCommandVisitor;

public class UnrecognizedCommand implements ShellCommand {

	UnrecognizedCommand(String chatId, String command) {
		
	}
	
	@Override
	public String getText() {
		throw new RuntimeException("NOT IMPLEMENTED");
	}
	
	@Override
	public void acceptSentRequest(ShellCommandVisitor visitor) {
		// dont relay to visitor
	}
}
