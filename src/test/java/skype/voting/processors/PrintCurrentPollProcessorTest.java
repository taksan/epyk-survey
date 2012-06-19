package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.ReplyListenerMock;

public class PrintCurrentPollProcessorTest {
	@Test
	public void onProcessMessage_ShoulsPrintPoll()
	{
		ProcessorTestUtils utils = new ProcessorTestUtils();
		PrintCurrentPollProcessor subject = new PrintCurrentPollProcessor();
		ReplyListenerMock listener = utils.initializeProcessorWithVotingSessionAndGetListener(subject);
		
		subject.processMessage(utils.getSessionChat(), "#showpoll");
		assertEquals("\n" + 
				"Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n" + 
				"Voters: tatu,uruca\n", 
				listener.reply.get() + "");
	}
}