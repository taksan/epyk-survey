package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.ReplyListenerMock;
import skype.voting.requests.StartPollRequest;

public class StartPollProcessorTest {
	@Test
	public void onProcessSentRequest_ShouldReturnVotingMenu() {
		StartPollProcessor subject = new StartPollProcessor();
		ProcessorTestUtils processorTestUtils = new ProcessorTestUtils();
		
		ReplyListenerMock listener = processorTestUtils.initializeProcessorAndGetListener(subject);
		
		StartPollRequest pollRequest = processorTestUtils.buildVotingPollRequest();
		subject.process(pollRequest);
		
		String expected = 
				"\n" + "Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n"  +
				"Voters: tatu,uruca\n";
		assertEquals(expected, listener.reply.get());
	}
}