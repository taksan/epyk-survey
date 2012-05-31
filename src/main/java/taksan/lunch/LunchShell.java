package taksan.lunch;

import taksan.skype.SkypeMessageListener;
import taksan.skype.SkypeMessageProvider;
import taksan.skype.shell.CommandInterpreter;

public class LunchShell implements SkypeMessageListener {

	private final CommandInterpreter interpreter;

	public LunchShell(SkypeMessageProvider skypeProvider, CommandInterpreter interpreter) {
		this.interpreter = interpreter;
		skypeProvider.addMessageListener(this);
	}

	public void onReceivedMessage(String message) {
		interpreter.processMessage(message);
	}

	@Override
	public void onSentMessage(String message) {
		interpreter.processMessage(message);
	}

}