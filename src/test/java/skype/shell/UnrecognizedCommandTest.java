package skype.shell;

import skype.voting.requests.RequestTest;

public class UnrecognizedCommandTest extends RequestTest {

	@Override
	protected ShellCommand getSubject() {
		return new UnrecognizedCommand(null, null);
	} 
}