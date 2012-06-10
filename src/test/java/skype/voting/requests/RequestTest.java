package skype.voting.requests;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.Assert;
import org.junit.Test;

import skype.shell.CommandProcessor;
import skype.shell.ShellCommand;

public abstract class RequestTest {

	public class CommandProcessorIH implements InvocationHandler {
		public ShellCommand receivedRequest = null;
		@Override
		public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable {
			receivedRequest = (ShellCommand) arg2[0];
			return null;
		}
	}

	final CommandProcessorIH processorIH = new CommandProcessorIH();
	CommandProcessor processor =
			(CommandProcessor) Proxy.newProxyInstance(
					RequestTest.class.getClassLoader(), 
					new Class[]{CommandProcessor.class}, 
					processorIH);
	
	@Test
	public void onAcceptProcessorForReceivedMessages_ShouldInvokeVisitorOnClosePollRequest(){
		ShellCommand subject = getSubject();
		subject.beProcessedAsReceivedMessage(processor);
		Assert.assertEquals(subject, processorIH.receivedRequest);
	}
	

	@Test
	public void onAcceptProcessorForSentMessages_ShouldInvokeVisitorOnVoteRequest()
	{
		ShellCommand subject = getSubject();
		subject.beProcessedAsSentMessage(processor);
		Assert.assertEquals(subject, processorIH.receivedRequest);
	}
	
	protected abstract ShellCommand getSubject();
}
