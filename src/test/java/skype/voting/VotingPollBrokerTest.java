package skype.voting;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandInterpreter;
import skype.shell.CommandProcessor;
import skype.shell.CommandProcessorAdapter;
import skype.shell.ReplyListener;
import skype.shell.ReplyTextRequest;
import skype.shell.ShellCommand;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.requests.MissingVotersRequest;

import com.skype.ChatListener;
import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.User;

public class VotingPollBrokerTest {
	final class CommandProcessorAdapterMock extends CommandProcessorAdapter {
		public ReplyListener listener;

		@Override
		public void addReplyListener(ReplyListener listener) {
			this.listener = listener;
			operations.append("addReplyListener\n");
		}

		@Override
		public void processMissingVoteRequest(MissingVotersRequest missingVotersRequest) {
			throw new RuntimeException("NOT IMPLEMENTED");
		}

		@Override
		public void processReplyTextRequest(ReplyTextRequest replyTextRequest) {
			throw new RuntimeException("NOT IMPLEMENTED");
		}
	}

	private CommandInterpreter interpreterMock;
	private CommandProcessorAdapterMock processorMock;
	StringBuilder operations = new StringBuilder();

	@Test
	public void onChatMessageReceived_ShouldInterpretAndSendToProcessor() throws SkypeException
	{
		VotingPollBroker subject = new VotingPollBroker(getBridge(), getInterpreter(), getProcessor());
		subject.chatMessageReceived(null);
		assertEquals(
				"addReplyListener\n" + 
				"getChatAdapter\n" + 
				"getContent\n" + 
				"ShellCommand beProcessedAsReceivedMessage\n" + 
				"", operations.toString());
	}
	
	@Test
	public void onChatMessageSent_ShouldInterpretAndSendToProcessor() throws SkypeException
	{
		VotingPollBroker subject = new VotingPollBroker(getBridge(), getInterpreter(), getProcessor());
		subject.chatMessageSent(null);
		assertEquals(
				"addReplyListener\n" + 
				"getChatAdapter\n" + 
				"getContent\n" + 
				"ShellCommand beProcessedAsSentMessage\n" + 
				"", operations.toString());
	}
	
	@Test
	public void onReplyListenerInvoke_ShouldInvokeSkypeBridgeSendMessage()
	{
		CommandProcessorAdapterMock processor = getProcessor();
		new VotingPollBroker(getBridge(), getInterpreter(), processor);
		processor.listener.onReply(null, "foo");
		
		assertEquals("addReplyListener\n" + 
				"SkypeBridge: sendMessage: foo", 
				operations.toString());
	}
	
	@Test
	public void onReplyListenerPrivateInvoke_ShouldInvokeSkypeBridgeSendMessageToSender()
	{
		CommandProcessorAdapterMock processor = getProcessor();
		new VotingPollBroker(getBridge(), getInterpreter(), processor);
		ChatAdapterInterface chat = new ChatBridgeMock("", "wauss");
		processor.listener.onReplyPrivate(chat, "foo");
		
		assertEquals("addReplyListener\n" + 
				"sendMessageToUser: wauss message:foo", 
				operations.toString());
	}

	private CommandProcessorAdapterMock getProcessor() {
		if (processorMock != null)
			return processorMock;
		processorMock = new CommandProcessorAdapterMock();
		return processorMock;
	}

	private CommandInterpreter getInterpreter() {
		if (interpreterMock != null)
			return interpreterMock;
		
		interpreterMock = new CommandInterpreter() {
			
			@Override
			public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
				return new AbstractShellCommand(chat,message) {
					
					@Override
					public void beProcessedAsSentMessage(CommandProcessor processor) {
						operations.append("ShellCommand beProcessedAsSentMessage\n");
					}
					
					@Override
					public void beProcessedAsReceivedMessage(CommandProcessor processor) {
						operations.append("ShellCommand beProcessedAsReceivedMessage\n");
					}
				};
			}
		};
		return interpreterMock;
	}

	private SkypeBridge getBridge() {
		return new SkypeBridge() {
			
			@Override
			public void sendMessage(ChatAdapterInterface chatBridgeInterface, String message) {
				operations.append("SkypeBridge: sendMessage: " + message);
			}
			
			@Override
			public String getUserFullNameOrId(User sender) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
			@Override
			public ChatAdapterInterface getChatAdapter(ChatMessage sentChatMessage) {
				operations.append("getChatAdapter\n");
				return new ChatAdapterInterface() {
					
					@Override
					public void setTopic(String topic) {
						throw new RuntimeException("NOT IMPLEMENTED");
					}
					
					@Override
					public void setLastSender(String senderFullNameOrId) {
						throw new RuntimeException("NOT IMPLEMENTED");
					}
					
					@Override
					public void setGuidelines(String guidelines) {
						throw new RuntimeException("NOT IMPLEMENTED");
					}
					
					@Override
					public void removeListener(ChatListener weakReference) {
						throw new RuntimeException("NOT IMPLEMENTED");
					}
					
					@Override
					public SkypeBridge getSkypeBridge() {
						throw new RuntimeException("NOT IMPLEMENTED");
					}
					
					@Override
					public List<String> getPartipantNames() {
						throw new RuntimeException("NOT IMPLEMENTED");
					}
					
					@Override
					public String getLasterSenderFullName() {
						throw new RuntimeException("NOT IMPLEMENTED");
					}
					
					@Override
					public void addListener(ChatListener listener) {
						throw new RuntimeException("NOT IMPLEMENTED");
					}
				};
			}

			@Override
			public String getContent(ChatMessage receivedChatMessage) {
				operations.append("getContent\n");
				return "";
			}

			@Override
			public void sendMessageToUser(String user, String message) {
				operations.append("sendMessageToUser: " +user +" message:" + message);
			}
		};
	}
}