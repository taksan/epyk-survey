package skype.voting.requests.factories;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;

public class VoteStatusInterpreterTest {
	VoteStatusInterpreter subject = new VoteStatusInterpreter();
	
	@Test
	public void onProduce_ShouldParseCommandAndCreateRequest()
	{
		ChatAdapterInterface chat = new ChatBridgeMock("42","moe");
		assertTrue(subject.understands("#sTatus"));
		subject.processMessage(chat, "#status");
	}
}