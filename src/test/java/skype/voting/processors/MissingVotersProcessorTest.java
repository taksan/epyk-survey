package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.ReplyListenerMock;
import skype.voting.requests.MissingVotersRequest;

public class MissingVotersProcessorTest { 
	MissingVotersProcessor subject = new MissingVotersProcessor();
	
	ProcessorTestUtils processorTestUtils;
	
	public MissingVotersProcessorTest() {
		processorTestUtils = new ProcessorTestUtils();
	}
	
	@Test
	public void onProcessMissingVotersRequest_ShouldPrintWhoHasnVoted()
	{
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionAndGetListener(subject);
		MissingVotersRequest request = new  MissingVotersRequest(processorTestUtils.getSessionChat(), null);
		subject.process(request);
		assertEquals("Users that haven't voted yet:\n" +
				"	tatu, uruca", listener.reply.get() + "");
	}
	

	@Test
	public void onProcessMissingVotersRequestWithoutAnyone_ShouldPrintEveryoneVoted(){
		ReplyListenerMock listener = processorTestUtils.initializeProcessorWithVotingSessionWhereEveryoneVotedAndGetListener(subject);
		MissingVotersRequest request = new  MissingVotersRequest(processorTestUtils.getSessionChat(), null);
		subject.process(request);
		assertEquals("Everyone already voted.", listener.reply.get() + "");
	}
	
	@Test
	public void onProcessMissingVotersRequestWithClosedSession_ShouldNotGenerateReply()
	{
		final ReplyListenerMock listener = processorTestUtils.initializeProcessorAndGetListener(subject);
		MissingVotersRequest request = new MissingVotersRequest(processorTestUtils.getSessionChat(), null);
		subject.process(request);
		assertEquals("", listener.reply.get());
	}

}