package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.UnrecognizedCommand;
import skype.voting.ReplyListenerMock;

public class UnrecognizedVotingProcessorTest { 
	UnrecognizedVotingProcessor subject = new UnrecognizedVotingProcessor();
	ProcessorTestUtils processorTestUtils = new ProcessorTestUtils();
	ReplyListenerMock listener = processorTestUtils.initializeProcessorAndGetListener(subject);
	
	@Test
	public void onProcessUnrecognizedCommand_ShouldReturnErrorMessage() {
		subject.process(new UnrecognizedCommand(null, "#commandfoo"));
		String expected = "'#commandfoo' not recognized";
		assertEquals(expected, listener.replyPrivate.get());
	}
	

	@Test
	public void onprocessIfPossibleWithoutLeadingSharp_ShouldNotGenerateReply() {
		subject.process(new UnrecognizedCommand(null, "commandfoo"));
		assertEquals("", listener.reply.get());
	}

}