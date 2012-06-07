package skype.voting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.ReplyListener;
import skype.shell.UnrecognizedCommand;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.VotingPollOption;
import skype.voting.VotingPollProcessor;
import skype.voting.VotingPollRequest;
import skype.voting.VoteRequest;

public class VotingPollProcessorTest { 
	VotingPollProcessor subject = new VotingPollProcessor();
	ReplyListener listener;
	final AtomicReference<String> reply = new AtomicReference<String>();
	
	public VotingPollProcessorTest() {
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
		buildVotingPollRequest();
		
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
		buildVotingPollRequest();

		VoteRequest voteRequest = new VoteRequest("_foo_user_", 2);
		subject.processVoteRequest(voteRequest);
		
		assertEquals("Votes: foo: 0 ; baz: 1", reply.get()+"");
	}

	private void buildVotingPollRequest() {
		ChatBridgeMock chat = new ChatBridgeMock("autoid");
		
		VotingPollRequest request = new VotingPollRequest(chat);
		request.add(new VotingPollOption("foo"));
		request.add(new VotingPollOption("baz"));
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