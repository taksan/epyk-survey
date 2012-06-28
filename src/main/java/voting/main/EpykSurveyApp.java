package voting.main;

import skype.SkypeBridgeImpl;
import skype.alias.AliasCommandExecutor;
import skype.voting.CommandExecutor;
import skype.voting.VotingPollBroker;
import skype.voting.VotingPollCommandExecutor;
import skype.voting.processors.interpreters.VotingFactoriesRetriever;

import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;

public class EpykSurveyApp {

	public void execute() throws SkypeException {
		
		CommandExecutor[] botExecutors = getCommandExecutors();
		
		VotingPollBroker listener = new VotingPollBroker(
				SkypeBridgeImpl.get(), 
				botExecutors);
		
		Skype.addChatMessageListener(listener);
		User.getInstance("echo123").send("Epyk Survey Started!");
	}

	private static CommandExecutor[] getCommandExecutors() {
		final VotingPollCommandExecutor votingPollExecutor = new VotingPollCommandExecutor();
		final AliasCommandExecutor aliasExecutor = 
				new AliasCommandExecutor(VotingFactoriesRetriever.getSingletonAliasExpander());
		CommandExecutor[] botExecutors = new CommandExecutor[]{aliasExecutor, votingPollExecutor};
		return botExecutors;
	}
}
