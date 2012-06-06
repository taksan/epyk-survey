package skype.lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;
import skype.shell.UnrecognizedCommand;
import skype.shell.mocks.ChatBridgeMock;

public class LunchProcessorTest { 
	LunchProcessor subject = new LunchProcessor();
	ReplyListener listener;
	final AtomicReference<String> reply = new AtomicReference<String>();
	
	public LunchProcessorTest() {
		listener = new ReplyListener() {
			public void onReply(ChatAdapterInterface chat, String replyMessage) {
				reply.set(replyMessage);
			}
		};
		subject.addReplyListener(listener);
	}
	
	@Test
	public void onProcessSentRequest_ShouldReturnVotingMenu()
	{
		buildLunchRequest();
		
		String expected =
				"\n" +
				"Almoço!\n" +
				"1) foo\n" +
				"2) baz";
		assertEquals(expected, reply.get());
	}
	
	@Test
	public void onProcessVoteWithoutLunchSession_ShouldDoNothing()
	{
		VoteRequest request = new VoteRequest("_foo_user_", 1);
		subject.processVoteRequest(request);
		assertNull(reply.get());
	}
	
	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueUserAndVoteMessage()
	{
		buildLunchRequest();

		VoteRequest voteRequest = new VoteRequest("_foo_user_", 2);
		subject.processVoteRequest(voteRequest);
		
		assertEquals("Votes: foo: 0 ; baz: 1", reply.get()+"");
	}

	private void buildLunchRequest() {
		ChatBridgeMock chat = new ChatBridgeMock("autoid");
		
		LunchRequest request = new LunchRequest(chat);
		request.add(new LunchOption("foo"));
		request.add(new LunchOption("baz"));
		subject.processLunchRequest(request);
	}
	
	@Test
	public void onProcessUnrecognizedCommand_ShouldReturnErrorMessage()
	{
		subject.processUnrecognizedCommand(new UnrecognizedCommand(null, "#commandfoo"));
		String expected = "'#commandfoo' not recognized";
		assertEquals(expected, reply.get());
	}
	
	@Test
	public void onProcessUnrecognizedCommandWithoutLeadingSharp_ShouldDoNothing() {
		subject.processUnrecognizedCommand(new UnrecognizedCommand(null, "commandfoo"));
		assertNull(reply.get());
	}
}