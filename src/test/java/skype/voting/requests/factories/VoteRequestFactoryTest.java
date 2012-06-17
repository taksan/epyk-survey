package skype.voting.requests.factories;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.requests.VoteRequest;
import skype.voting.requests.factories.VoteRequestFactory;

public class VoteRequestFactoryTest {
	VoteRequestFactory subject = new VoteRequestFactory();
	ChatAdapterInterface chat = new ChatBridgeMock("42","moe");
	
	@Test
	public void onVoteCommand_ShouldGenerateVoteRequest(){
		VoteRequest request = subject.processMessage(chat, " #3");
		assertEquals(request.sender, "moe");
		assertEquals(request.vote, 3);
	}
}