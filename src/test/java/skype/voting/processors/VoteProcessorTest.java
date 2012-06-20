package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.ReplyListenerMock;
import skype.voting.mocks.VoteRequestMocked;
import skype.voting.processors.requests.VoteRequest;

public class VoteProcessorTest { 
	VoteProcessor subject = new VoteProcessor();
	
	ProcessorTestUtils processorTestUtils;
	
	public VoteProcessorTest() {
		processorTestUtils = new ProcessorTestUtils();
	}
	
	@Test
	public void onProcessVoteWithoutLunchSession_ShouldNotGenerateReply() {
		ReplyListenerMock listener = processorTestUtils.initializeProcessorAndGetListener(subject);
		
		subject.processMessage(processorTestUtils.getSessionChat(), "#1");
		assertEquals("", listener.reply.get());
	}
	
	public void onProcessVote_ShouldIssueVote()
	{
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		
		subject.processMessage(processorTestUtils.getSessionChat(), "#2");
		assertEquals("Votes: foo: 0 ; baz: 1", listener.reply.get() + "");
	}
	
	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueUserAndVoteMessage() {
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		
		subject.processMessage(processorTestUtils.getSessionChat(), "#2");

		assertEquals("Votes: <getVotingStatusMessage>", listener.reply.get() + "");
	}

	@Test
	public void onProcessVoteWithLuncSession_ShouldIssueErrorIfVoteIsInvalid() {
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		
		subject.processMessage(processorTestUtils.getSessionChat(), "#42");

		assertEquals("Invalid voting option 42. Valid options:<buildVotingMenu>",
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