package skype.voting.processors.interpreters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.processors.interpreters.MissingVotersInterpreter;
import skype.voting.processors.requests.MissingVotersRequest;

public class MissingVotersInterpreterTest {
	@Test
	public void onProcuce_ShouldCreateRequestInstance()
	{
		MissingVotersInterpreter subject = new MissingVotersInterpreter();
		assertTrue(subject.understands("#missing"));
		ChatAdapterInterface chat = new ChatBridgeMock("42","moe");
		MissingVotersRequest request = subject.processMessage(chat, "#missing");
		assertNotNull(request);
	}
}
