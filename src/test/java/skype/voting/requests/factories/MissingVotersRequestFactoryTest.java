package skype.voting.requests.factories;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.requests.MissingVotersRequest;

public class MissingVotersRequestFactoryTest {
	@Test
	public void onProcuce_ShouldCreateRequestInstance()
	{
		MissingVotersRequestFactory subject = new MissingVotersRequestFactory();
		assertTrue(subject.understands("#missing"));
		ChatAdapterInterface chat = new ChatBridgeMock("42","moe");
		MissingVotersRequest request = subject.processMessage(chat, "#missing");
		assertNotNull(request);
	}
}
