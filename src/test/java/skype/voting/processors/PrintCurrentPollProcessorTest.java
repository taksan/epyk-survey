package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.ReplyListenerMock;

public class PrintCurrentPollProcessorTest {
	PrintCurrentPollProcessor subject = new PrintCurrentPollProcessor();
	
	@Test
	public void onProcessMessage_ShoulsPrintPoll()
	{
		ProcessorTestUtils utils = new ProcessorTestUtils();
		ReplyListenerMock listener = utils.initializeProcessorWithVotingSessionAndGetListener(subject);
		
		subject.processMessage(utils.getSessionChat(), "#showpoll");
		assertEquals("<getUpdatedVotingMenu>", listener.reply.get() + "");
	}
	
	@Test
	public void onProcessWithUninitializedPoll_ShouldDoNothing(){
		ProcessorTestUtils utils = new ProcessorTestUtils();
		ReplyListenerMock listener = utils.initializeProcessorAndGetListener(subject);
		
		subject.processMessage(utils.getSessionChat(), "#showpoll");
		assertEquals("", listener.replyPrivate.get());
		assertEquals("", listener.reply.get());
	}
}