package skype.voting.requests.factories;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.requests.ClosePollRequest;
import skype.voting.requests.factories.ClosePollRequestFactory;

public class ClosePollRequestFactoryTest {
	ClosePollRequestFactory subject = new ClosePollRequestFactory();
	ChatAdapterInterface chat = new ChatBridgeMock("43");
	
	@Test
	public void onCommand_ShouldCreateAClosePollRequest()
	{
		ClosePollRequest request = subject.produce(chat , "#closepoll");
		assertNotNull(request);
		
		ClosePollRequest request2 = subject.produce(chat , "#closepoll ");
		assertNotNull(request2);
		
		ClosePollRequest request3 = subject.produce(chat , "#Closepoll ");
		assertNotNull(request3);
	}
	
	@Test
	public void OnInvalidCommand_ShouldReturnNull()
	{
		ClosePollRequest request = subject.produce(chat , "#cslosepoll");
		assertNull(request);
	}
}