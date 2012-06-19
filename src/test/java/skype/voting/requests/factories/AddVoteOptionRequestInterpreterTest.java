package skype.voting.requests.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;
import skype.voting.requests.AddVoteOptionRequest;

public class AddVoteOptionRequestInterpreterTest {
	AddVoteOptionRequestInterpreter subject = new AddVoteOptionRequestInterpreter();
	
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
