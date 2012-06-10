package voting.main;

import skype.SkypeBridgeImpl;
import skype.shell.CommandInterpreterImpl;
import skype.shell.ShellCommandFactory;
import skype.voting.VotingPollBroker;
import skype.voting.VotingPollCommandProcessor;
import skype.voting.requests.factories.VotingFactoriesRetriever;

import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.connector.Connector;

public class VotingTimeApp {

	public void execute() throws SkypeException {
		Connector.getInstance().setApplicationName("LunchTime");
		
		VotingPollBroker listener = new VotingPollBroker(
				SkypeBridgeImpl.get(), 
				getInterpreter(), 
				new VotingPollCommandProcessor());
		
		Skype.addChatMessageListener(listener);
	}

	private CommandInterpreterImpl getInterpreter() {
		ShellCommandFactory[] factories = VotingFactoriesRetriever.getFactories();
		return new CommandInterpreterImpl(factories);
	}
}
