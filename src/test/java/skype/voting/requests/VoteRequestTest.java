package skype.voting.requests;

import skype.shell.ShellCommand;
import skype.shell.mocks.ChatBridgeMock;


public class VoteRequestTest extends RequestTest {
	
	@Override
	protected ShellCommand getSubject() {
		return new VoteRequest(new ChatBridgeMock(), "foo", 1);
	}

}