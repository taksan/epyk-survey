package skype.voting.requests;

import org.junit.Assert;
import org.junit.Test;

import skype.shell.ShellCommand;
import skype.voting.requests.mocks.CommandProcessorMock;

public abstract class RequestTest {
	CommandProcessorMock processor = new CommandProcessorMock();
	@Test
	public void onAcceptProcessorForReceivedMessages_ShouldInvokeVisitorOnClosePollRequest(){
		ShellCommand subject = getSubject();
		subject.acceptProcessorForReceivedMessages(processor);
		Assert.assertEquals(subject, processor.receivedRequest);
	}
	

	@Test
	public void onAcceptProcessorForSentMessages_ShouldInvokeVisitorOnVoteRequest()
	{
		ShellCommand subject = getSubject();
		subject.acceptProcessorForSentMessages(processor);
		Assert.assertEquals(subject, processor.receivedRequest);
	}
	
	protected abstract ShellCommand getSubject();
}
