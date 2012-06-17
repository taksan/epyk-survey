package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.ReplyTextRequest;
import skype.voting.ReplyListenerMock;

public class ReplyTextVotingProcessorTest { 
	
	@Test
	public void onprocessIfPossible_ShouldReplyTextInRequest()
	{
		ReplyTextVotingProcessor subject = new ReplyTextVotingProcessor();
		ProcessorTestUtils processorTestUtils = new ProcessorTestUtils();
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		ReplyTextRequest request = new ReplyTextRequest(processorTestUtils.getSessionChat(), null, "some text");
		subject.process(request);
		assertEquals("some text", listener.reply.get());
	}
}