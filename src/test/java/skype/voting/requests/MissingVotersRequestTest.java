package skype.voting.requests;

import skype.shell.ShellCommand;
import skype.shell.mocks.ChatBridgeMock;

public class MissingVotersRequestTest extends RequestTest {

	@Override
	protected ShellCommand getSubject() {
		return new MissingVotersRequest(new ChatBridgeMock("foo"), "#missing");
	}

}
