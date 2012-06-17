package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.ReplyListenerMock;
import skype.voting.VotingPollCommandExecutor;
import skype.voting.requests.HelpRequest;

public class HelpProcessorTest { 
	HelpProcessor subject = new HelpProcessor();
	ProcessorTestUtils utils = new ProcessorTestUtils();
	
	@Test
	public void onProcessHelpRequest_ShouldGenerateReplyWithHelpMessage()
	{
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
	
	@Test
	public void onProcessHelpRequestWithouInitializedPoll_ShouldDoNothing()
	{
		ReplyListenerMock listener = utils.initializeProcessorAndGetListener(subject);
		HelpRequest request = new HelpRequest(null, null);
		subject.process(request);
		assertEquals("", listener.replyPrivate.get());
		assertEquals("", listener.reply.get());
	}

}