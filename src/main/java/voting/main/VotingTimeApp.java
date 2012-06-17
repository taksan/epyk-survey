package voting.main;

import skype.ChatAdapterInterface;
import skype.SkypeBridgeImpl;
import skype.shell.CommandInterpreterImpl;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;
import skype.voting.CommandExecutor;
import skype.voting.MainCommandExecutor;
import skype.voting.VotingPollBroker;
import skype.voting.VotingPollCommandExecutor;

import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import com.skype.connector.Connector;

public class VotingTimeApp {

	public void execute() throws SkypeException {
		Connector.getInstance().setApplicationName("LunchTime");
		final VotingPollCommandExecutor executor = new VotingPollCommandExecutor();
		final CommandInterpreterImpl interpreter = new CommandInterpreterImpl();
		CommandExecutor[] processorUnits = new CommandExecutor[]{
				new CommandExecutor() {
					@Override
					public void setReplyListener(ReplyListener listener) {
						executor.setReplyListener(listener);
					}
					
					@Override
					public boolean processMessage(ChatAdapterInterface chat, String message) {
						ShellCommand shellCommand = interpreter.processMessage(
								chat,
								message);
						
						return executor.processIfPossible(shellCommand);
					}
				}
		};
		MainCommandExecutor executorImplementation = 
				new MainCommandExecutor(processorUnits);
		
		VotingPollBroker listener = new VotingPollBroker(
				SkypeBridgeImpl.get(), 
				new MainCommandExecutor[]{executorImplementation});
		
		Skype.addChatMessageListener(listener);
		User.getInstance("echo123").send("Voting app on!");
	}
}
