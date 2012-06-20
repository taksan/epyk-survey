package skype.voting.processors.interpreters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.processors.interpreters.VoteInterpreter;
import skype.voting.processors.requests.VoteRequest;

public class VoteInterpreterTest {
	VoteInterpreter subject = new VoteInterpreter();
	ChatAdapterInterface chat = new ChatBridgeMock("42","moe");
	
	@Test
	public void onVoteCommand_ShouldGenerateVoteRequest(){
		VoteRequest request = subject.processMessage(chat, " #3");
		assertEquals(request.sender, "moe");
		assertEquals(request.vote, 3);
	}
	
	@Test
	public void onVoteCommand_ShouldGenerateAcceptNumber(){
		VoteRequest request = subject.processMessage(chat, " 3");
		assertEquals(request.sender, "moe");
		assertEquals(request.vote, 3);
	}
	
	@Test
	public void onInvalidCommand_ShouldReturnNull() {
		assertNull(subject.processMessage(chat, "foo"));
	}
}