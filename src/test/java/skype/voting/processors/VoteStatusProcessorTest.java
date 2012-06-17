package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.ReplyListenerMock;
import skype.voting.requests.VoteStatusRequest;

public class VoteStatusProcessorTest {
	
	@Test
	public void onProcessVoteStatusRequest_ShouldPrintVotingStatusForGivenChat()
	{
		ProcessorTestUtils processorTestUtils = new ProcessorTestUtils();
		VoteStatusProcessor subject = new VoteStatusProcessor();
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		VoteStatusRequest voteStatusRequest = new VoteStatusRequest(processorTestUtils.getSessionChat(), null);
		subject.process(voteStatusRequest);
		
		assertEquals("Votes: foo: 0 ; baz: 0", listener.replyPrivate.get());
	}
}