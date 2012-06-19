package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.voting.ReplyListenerMock;

public class AddVoteOptionProcessorTest { 
	ProcessorTestUtils utils = new ProcessorTestUtils();
	AddVoteOptionProcessor subject = new AddVoteOptionProcessor();
	ReplyListenerMock listener = utils.initializeProcessorWithVotingSessionAndGetListener(subject);
	
	@Test
	public void onProcess_ShouldAddVotingOption(){
		utils.getSessionChat().setLastSender("tatu");
		subject.processMessage(utils.getSessionChat(), "#addoption matre mia");
		
		assertEquals("New option 'matre mia' added by tatu. Current options:\n" +
				"Almoço!\n" +
				"1) foo\n" + 
				"2) baz\n" +
				"3) matre mia\n"+
				"Voters: tatu,uruca", 
				listener.reply.get());
	}

	@Test
	public void onProcessThatAlreadyExists_ShouldNotAddAndShouldWarnUser()
	{
		utils.getSessionChat().setLastSender("tatu");
		subject.processMessage(utils.getSessionChat(), "#addoption foo");
		
		assertEquals("Option 'foo' already added.", listener.replyPrivate.get());
	}
	
	@Test
	public void onProcessWithUninitializedPoll_ShouldDoNothing(){
		ProcessorTestUtils utils = new ProcessorTestUtils();
		AddVoteOptionProcessor subject = new AddVoteOptionProcessor();
		ReplyListenerMock listener = utils.initializeProcessorAndGetListener(subject);
		
		utils.getSessionChat().setLastSender("tatu");
		subject.processMessage(utils.getSessionChat(), "#addoption matre mia");
		assertEquals("", listener.replyPrivate.get());
		assertEquals("", listener.reply.get());
	}

}