package skype.voting.requests;

import skype.shell.ShellCommand;
import skype.shell.mocks.ChatBridgeMock;

public class VoteStatusRequestTest extends RequestTest {

	@Override
	protected ShellCommand getSubject() {
		return new VoteStatusRequest(new ChatBridgeMock("foo"), "baz");
	} 
	
}