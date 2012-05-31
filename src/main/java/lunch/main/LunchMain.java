package lunch.main;

import skype.lunch.CommandInterpreterImpl;
import skype.lunch.LunchRequestFactory;
import skype.lunch.LunchShell;

import com.skype.Skype;
import com.skype.SkypeException;

public class LunchMain {
	public static void main(String[] args) throws SkypeException {
		if (!Skype.isRunning()) {
			System.out.println("Skype must be running to start this application");
			System.exit(-1);
		}
		
		Skype.addChatMessageListener(new LunchShell(getInterpreter(), new SkypeBridgeImpl()));
	}

	private static CommandInterpreterImpl getInterpreter() {
		return new CommandInterpreterImpl(new LunchRequestFactory());
	}
}
