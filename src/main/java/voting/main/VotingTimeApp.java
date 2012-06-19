package voting.main;

import skype.SkypeBridgeImpl;
import skype.shell.AliasProcessorImpl;
import skype.voting.CommandExecutor;
import skype.voting.MainCommandExecutor;
import skype.voting.VotingPollBroker;
import skype.voting.VotingPollCommandExecutor;
import skype.voting.requests.factories.VotingFactoriesRetriever;

import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import com.skype.connector.Connector;

public class VotingTimeApp {

	public void execute() throws SkypeException {
		Connector.getInstance().setApplicationName("LunchTime");
		final VotingPollCommandExecutor executor = new VotingPollCommandExecutor();
		final AliasProcessorImpl aliasExecutor = new AliasProcessorImpl(VotingFactoriesRetriever.getSingletonAliasExpander());
		CommandExecutor[] processorUnits = new CommandExecutor[]{
				aliasExecutor, executor};
		MainCommandExecutor executorImplementation = 
				new MainCommandExecutor(processorUnits);
		
		VotingPollBroker listener = new VotingPollBroker(
				SkypeBridgeImpl.get(), 
				new MainCommandExecutor[]{executorImplementation});
		
		Skype.addChatMessageListener(listener);
		User.getInstance("echo123").send("Voting app on!");
	}
}
