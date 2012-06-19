package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.ReplyListenerMock;
import skype.voting.requests.ClosePollRequest;

public class ClosePollProcessorTest { 
	ProcessorTestUtils processorTestUtils;
	
	public ClosePollProcessorTest() {
		processorTestUtils = new ProcessorTestUtils();
	}
	
	@Test
	public void onClosePollRequest_ShouldPrintTheResults()
	{
		
		ClosePollProcessor subject = new ClosePollProcessor();
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAnd1VoteAndGetListener(subject);
		subject.process(getCloseRequest());
		assertEquals(
				"Votes: <getVotingStatusMessage>\n" + 
				"WINNER: ***baz*** with 1 vote", 
				listener.reply.get());
	}
	
	@Test
	public void onClosePollRequestWithTie_ShouldPrintTieResults()
	{
		ClosePollProcessor subject = new ClosePollProcessor();
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		subject.processMessage(processorTestUtils.getSessionChat(), "#closepoll");
		assertEquals(
				"Votes: <getVotingStatusMessage>\n" + 
				"TIE: **foo and baz** tied with 0 votes", 
				listener.reply.get());
	}
	
	@Test
	public void onClosePollAfterClosedPoll_ShouldGenerateNoReply()
	{
		ClosePollProcessor subject = new ClosePollProcessor();
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		subject.processMessage(processorTestUtils.getSessionChat(), "#closepoll");
		listener.reply.set("");
		subject.processMessage(processorTestUtils.getSessionChat(), "#closepoll");
		assertEquals(
				"", 
				listener.reply.get());
	}

	private ClosePollRequest getCloseRequest() {
		return new ClosePollRequest(processorTestUtils.getSessionChat(), null);
	}

}