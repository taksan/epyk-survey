package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.ReplyListenerMock;

public class VoteStatusProcessorTest {
	
	@Test
	public void onProcessVoteStatusRequest_ShouldPrintVotingStatusForGivenChat()
	{
		ProcessorTestUtils processorTestUtils = new ProcessorTestUtils();
		VoteStatusProcessor subject = new VoteStatusProcessor();
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		subject.processMessage(processorTestUtils.getSessionChat(), "#status");
		
		assertEquals("Votes: <getVotingStatusMessage>", listener.replyPrivate.get());
	}
}