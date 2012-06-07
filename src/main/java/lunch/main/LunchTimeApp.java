package lunch.main;

import skype.SkypeBridgeImpl;
import skype.shell.CommandInterpreterImpl;
import skype.voting.VotingPollBroker;
import skype.voting.VotingPollProcessor;
import skype.voting.VotingPollFactory;

import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.connector.Connector;

public class LunchTimeApp {

	public void execute() throws SkypeException {
		Connector.getInstance().setApplicationName("LunchTime");
		
		VotingPollBroker listener = new VotingPollBroker(new SkypeBridgeImpl(), getInterpreter(), new VotingPollProcessor());
		Skype.addChatMessageListener(listener);
	}

	private CommandInterpreterImpl getInterpreter() {
		return new CommandInterpreterImpl(new VotingPollFactory());
	}
}
