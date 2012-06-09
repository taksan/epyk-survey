package skype.voting.requests;

import skype.shell.ShellCommand;


public class ClosePollRequestTest extends RequestTest {

	@Override
	protected ShellCommand getSubject() {
		return new ClosePollRequest(null,null);
	}
}