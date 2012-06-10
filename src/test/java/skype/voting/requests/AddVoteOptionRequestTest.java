package skype.voting.requests;

import skype.shell.ShellCommand;
import skype.shell.mocks.ChatBridgeMock;

public class AddVoteOptionRequestTest extends RequestTest {

	@Override
	protected ShellCommand getSubject() {
		return new AddVoteOptionRequest(new ChatBridgeMock("foo"), "#addoption", "some");
	}

}
