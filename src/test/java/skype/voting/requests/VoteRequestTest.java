package skype.voting.requests;

import skype.shell.ShellCommand;


public class VoteRequestTest extends RequestTest {
	
	@Override
	protected ShellCommand getSubject() {
		return new VoteRequest("foo", 1);
	}

}