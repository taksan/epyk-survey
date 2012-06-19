package skype.voting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;
import skype.shell.mocks.ChatBridgeMock;

public class UnrecognizedRequestExecutorTest {
	@Test
	public void onProcess_ShouldReplyUnrecognizedCommand() {
		UnrecognizedRequestExecutor subject = new UnrecognizedRequestExecutor();
		ReplyListenerMock listener = new ReplyListenerMock();
		subject.setReplyListener(listener);
		
		ChatBridgeMock chat = new ChatBridgeMock();
		subject.processMessage(chat, " #foo");
		assertEquals("Unrecognized command '#foo'", listener.replyPrivate.get());
	}
	
	@Test
	public void onProcessMessageWithoutSharp_ShouldNotReplyUnrecognizedCommand()
	{
		UnrecognizedRequestExecutor subject = new UnrecognizedRequestExecutor();
		ReplyListener listener = new ReplyListener() {
			@Override
			public void onReplyPrivate(ChatAdapterInterface chatAdapterInterface, String reply) {
				throw new RuntimeException("SHOULD NOT INVOKE");
			}
			@Override
			public void onReply(ChatAdapterInterface chatAdapterInterface, String reply) {
				throw new RuntimeException("SHOULD NOT INVOKE");
			}
		};
		subject.setReplyListener(listener);
		
		ChatBridgeMock chat = new ChatBridgeMock();
		subject.processMessage(chat, "foo");
	}
}