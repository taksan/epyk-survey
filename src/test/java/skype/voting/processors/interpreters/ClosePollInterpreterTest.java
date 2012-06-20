package skype.voting.processors.interpreters;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.processors.interpreters.ClosePollInterpreter;
import skype.voting.processors.requests.ClosePollRequest;

public class ClosePollInterpreterTest {
	ClosePollInterpreter subject = new ClosePollInterpreter();
	ChatAdapterInterface chat = new ChatBridgeMock("43");
	
	@Test
	public void onCommand_ShouldCreateAClosePollRequest()
	{
		ClosePollRequest request = subject.processMessage(chat , "#closepoll");
		assertNotNull(request);
		
		ClosePollRequest request2 = subject.processMessage(chat , "#closepoll ");
		assertNotNull(request2);
		
		ClosePollRequest request3 = subject.processMessage(chat , "#Closepoll ");
		assertNotNull(request3);
	}
	
	@Test
	public void OnInvalidCommand_ShouldReturnNull()
	{
		ClosePollRequest request = subject.processMessage(chat , "#cslosepoll");
		assertNull(request);
	}	
}