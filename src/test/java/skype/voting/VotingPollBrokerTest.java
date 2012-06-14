package skype.voting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.ReplyListener;
import skype.shell.mocks.ChatBridgeMock;

import com.skype.SkypeException;

public class VotingPollBrokerTest {

	@Test
	public void onChatMessageReceived_ShouldLetExecutorProcessIt() throws SkypeException
	{
		VotingPollBroker subject = new VotingPollBroker(getBridge(), getExecutors());
		subject.chatMessageReceived(null);
		assertEquals(
				"CommandExecutor setReplyListener\n" +
				"getChatAdapter\n" + 
				"getContent\n" + 
				"CommandExecutor processMessage", operations.toString());
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
				"CommandExecutor processMessage", operations.toString());
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
		public CommandExecutorMock(StringBuilder operations) {
			operations2 = operations;
		}

		@Override
		public boolean processMessage(ChatAdapterInterface chat, String message) {
			operations2.append("CommandExecutor processMessage");
			return true;
		}

		@Override
		public void setReplyListener(ReplyListener listener) {
			operations2.append("CommandExecutor setReplyListener\n");
		}
	}

	private CommandExecutorMock oneExecutor = new CommandExecutorMock(operations);

	
	private SkypeBridge getBridge() {
		return new SkypeBridgeMock(operations);
	}
}