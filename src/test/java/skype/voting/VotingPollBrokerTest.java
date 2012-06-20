package skype.voting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.HelpCommandExecutor;
import skype.shell.ReplyListener;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.mocks.CommmandExecutorAndHelperMock;

import com.skype.SkypeException;

public class VotingPollBrokerTest {
	@Test
	public void onCreation_ShouldAddHelpExecutorThatProcessAllExecutorsThatAreHelpProviders()
	{
		CommmandExecutorAndHelperMock e1 = new CommmandExecutorAndHelperMock("e1", operations);
		CommmandExecutorAndHelperMock e2 = new CommmandExecutorAndHelperMock("e2", operations);
		
		VotingPollBroker subject = new VotingPollBroker(getBridge(), e1, oneExecutor, e2);
		ArrayList<CommandExecutor> executors = subject.commandExecutors;
		HelpCommandExecutor e = null;
		for (CommandExecutor commandExecutor : executors) {
			if (commandExecutor instanceof HelpCommandExecutor) {
				e = (HelpCommandExecutor) commandExecutor;
			}
		}
		assertNotNull(e);
		e.processMessage(new ChatBridgeMock(), "#help");
		
		assertEquals(
				"e1 CommmandExecutorAndHelperMock setReplyListener\n" + 
				"CommandExecutor setReplyListener\n" + 
				"e2 CommmandExecutorAndHelperMock setReplyListener\n" +  
				"e1 acceptHelpVisitor\n" +
				"e2 acceptHelpVisitor\n" +
				"sendMessageToUser: none message:HELP.\n", 
				operations.toString());
	}
	
	@Test
	public void onChatMessageThatIsNotProcessedByAnyExecutors_ShouldReplyUnrecognizedCommand() throws SkypeException
	{
		doesNotUnderstandExecutor.setUnderstands(false);
		CommandExecutor[] commandExecutors = new CommandExecutor[]{doesNotUnderstandExecutor ,doesNotUnderstandExecutor};
		SkypeBridgeMock bridge = getBridge();
		
		UnrecognizedRequestExecutor unrecog = new UnrecognizedRequestExecutor() {
			@Override
			public boolean processMessage(ChatAdapterInterface chat, String messageRaw) {
				operations.append("Unrecognized command ''");
				return true;
			}
		};
		VotingPollBroker subject = new VotingPollBroker(bridge, unrecog , commandExecutors);
		subject.chatMessageReceived(null);
		
		String expected = 		
				"CommandExecutor setReplyListener\n" + 
				"CommandExecutor setReplyListener\n" +
				"getChatAdapter\n" + 
				"getContent\n" + 
				"CommandExecutor DOES NOT processMessage\n" + 
				"CommandExecutor DOES NOT processMessage\n" + 
				"Unrecognized command ''";
				
		assertEquals(expected, operations.toString());
	}

	@Test
	public void onChatMessageReceived_ShouldLetOnlyTheExecutorProcessIt() throws SkypeException
	{
		CommandExecutor[] commandExecutors = new CommandExecutor[]{oneExecutor,oneExecutor};
		VotingPollBroker subject = new VotingPollBroker(getBridge(), commandExecutors);
		subject.chatMessageReceived(null);
		assertEquals(
				"CommandExecutor setReplyListener\n" +
				"CommandExecutor setReplyListener\n" +
				"getChatAdapter\n" + 
				"getContent\n" + 
				"CommandExecutor processMessage\n",
				operations.toString());
	}
	
	@Test
	public void onChatMessageSent_ShouldLetExecutorProcessIt() throws SkypeException
	{
		VotingPollBroker subject = new VotingPollBroker(getBridge(), getExecutors());
		subject.chatMessageSent(null);
		assertEquals(
				"CommandExecutor setReplyListener\n" +
				"getChatAdapter\n" + 
				"getContent\n" + 
				"CommandExecutor processMessage\n", operations.toString());
	}
	
	@Test
	public void onReplyListenerInvoke_ShouldInvokeSkypeBridgeSendMessage()
	{
		VotingPollBroker subject = new VotingPollBroker(getBridge(), getExecutors());
		subject.onReply(new ChatBridgeMock(), "foo");
		
		assertEquals(
				"CommandExecutor setReplyListener\n" + 
				"SkypeBridge: sendMessage: foo", 
				operations.toString());
	}
	
	@Test
	public void onReplyListenerPrivateInvoke_ShouldInvokeSkypeBridgeSendMessageToSender()
	{
		VotingPollBroker subject = new VotingPollBroker(getBridge(), getExecutors());
		subject.onReplyPrivate(new ChatBridgeMock("#", "wauss"), "foo");
		
		assertEquals(
				"CommandExecutor setReplyListener\n" + 
				"sendMessageToUser: wauss message:foo", 
				operations.toString());
		
	}	
	
	private CommandExecutor[] getExecutors() {
		return new CommandExecutor[]{oneExecutor};
	}
	
	StringBuilder operations = new StringBuilder();
	
	private final static class CommandExecutorMock implements CommandExecutor {
		private final StringBuilder operations2;
		private boolean understands = true;
		public CommandExecutorMock(StringBuilder operations) {
			operations2 = operations;
		}

		public void setUnderstands(boolean b) {
			understands = b;
		}

		@Override
		public boolean processMessage(ChatAdapterInterface chat, String message) {
			if (!understands) {
				operations2.append("CommandExecutor DOES NOT processMessage\n");
				return false;
			}
			operations2.append("CommandExecutor processMessage\n");
			return true;
		}

		@Override
		public void setReplyListener(ReplyListener listener) {
			operations2.append("CommandExecutor setReplyListener\n");
		}
	}

	private CommandExecutorMock oneExecutor = new CommandExecutorMock(operations);
	private CommandExecutorMock doesNotUnderstandExecutor = new CommandExecutorMock(operations);

	
	private SkypeBridgeMock getBridge() {
		return new SkypeBridgeMock(operations);
	}
}