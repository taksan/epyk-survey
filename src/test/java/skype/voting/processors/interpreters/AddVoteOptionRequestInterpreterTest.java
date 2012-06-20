package skype.voting.processors.interpreters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;
import skype.voting.processors.interpreters.AddVoteOptionRequestInterpreter;
import skype.voting.processors.requests.AddVoteOptionRequest;

public class AddVoteOptionRequestInterpreterTest {
	AddVoteOptionRequestInterpreter subject = new AddVoteOptionRequestInterpreter();
	
	@Test
	public void onInvalidMessage_ShouldReturnNull()
	{
		assertNull(subject.processMessage(new ChatBridgeMock(), "#@addoption  pizza da mamma"));
	}
	
	@Test
	public void onAddOptionCommand_ShouldCreateAddVoteOptionRequest()
	{
		assertTrue(subject.understands("#addoption pizza"));
	}
	
	@Test
	public void onProduce_ShouldParseCommandAndCreateVoteOptionRequest(){
		AddVoteOptionRequest request = subject.processMessage(new ChatBridgeMock(), "#addoption  pizza da mamma");
		
		assertEquals("pizza da mamma", request.getName());
	}
}
