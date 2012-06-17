package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.ReplyListenerMock;
import skype.voting.requests.HelpRequest;

public class HelpProcessorTest { 
	@Test
	public void onProcessHelpRequest_ShouldGenerateReplyWithHelpMessage()
	{
		HelpProcessor subject = new HelpProcessor();
		ProcessorTestUtils utils = new ProcessorTestUtils();
		ReplyListenerMock listener = utils.initializeProcessorWithVotingSessionAndGetListener(subject);
		
		HelpRequest request = new HelpRequest(utils.getSessionChat(), null){
			@Override
			public String getHelpMessage() {
				return "help";
			}
		};
		subject.process(request);
		assertEquals("help\n", 
				listener.replyPrivate.get());
	}
}