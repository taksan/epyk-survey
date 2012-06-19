package skype.voting.processors;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;
import skype.voting.ReplyListenerMock;

public class StartPollProcessorTest {
	@Test
	public void onProcessSentRequest_ShouldReturnVotingMenu() {
		StartPollProcessor subject = new StartPollProcessor();
		ProcessorTestUtils processorTestUtils = new ProcessorTestUtils();
		
		ReplyListenerMock listener = processorTestUtils.initializeProcessorAndGetListener(subject);
		ChatBridgeMock sessionChat = processorTestUtils.getSessionChat();
		sessionChat.addParticipant("tatu");
		sessionChat.addParticipant("uruca");
		
		subject.processMessage(sessionChat, "#startpoll \"Almoço!\" foo,baz");
		
		String expected = 
				"\n" + "Almoço!\n" + 
				"1) foo\n" + 
				"2) baz\n"  +
				"Voters: tatu,uruca\n";
		assertEquals(expected, listener.reply.get());
	}
}