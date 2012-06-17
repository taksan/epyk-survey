package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.ReplyListenerMock;
import skype.voting.mocks.VoteRequestMocked;
import skype.voting.requests.VoteRequest;

public class VoteProcessorTest { 
	VoteProcessor subject = new VoteProcessor();
	
	ProcessorTestUtils processorTestUtils;
	
	public VoteProcessorTest() {
		processorTestUtils = new ProcessorTestUtils();
	}
	
	@Test
	public void onProcessVoteWithoutLunchSession_ShouldNotGenerateReply() {
		ReplyListenerMock listener = processorTestUtils.initializeProcessorAndGetListener(subject);
		
		VoteRequest request = new VoteRequestMocked("_foo_user_", 1);
		subject.process(request);
		assertEquals("", listener.reply.get());
	}
	
	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueUserAndVoteMessage() {
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		
		int vote = 2;
		VoteRequest anyVoteRequestWillPrintCurrentResults = processorTestUtils.makeVoteInInitializedChatSession(vote);
		subject.process(anyVoteRequestWillPrintCurrentResults);

		assertEquals("Votes: foo: 0 ; baz: 1", listener.reply.get() + "");
	}

	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueErrorIfVoteIsInvalid() {
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		
		VoteRequest invalidVote = processorTestUtils.makeVoteInInitializedChatSession(42);
		subject.process(invalidVote);

		assertEquals("Invalid voting option 42. Valid options:\n" + 
				"Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n" + 
				"Voters: tatu,uruca\n",
				listener.reply.get());
	}
	

	@Test
	public void onProcessVoteWithDifferentChatThanCurrentVoteRequest_ShouldNotIssueVote()
	{
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);

		ChatAdapterInterface chat2 = new ChatBridgeMock("anotherChat");
		VoteRequest anyVoteRequestWillPrintCurrentResults = new VoteRequestMocked(chat2 , 2);
		subject.process(anyVoteRequestWillPrintCurrentResults);
		
		assertEquals("", listener.reply.get());
	}

}