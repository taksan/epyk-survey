package skype.shell;

import skype.voting.requests.RequestTest;

public class ReplyTextRequestTest extends RequestTest {

	@Override
	protected ShellCommand getSubject() {
		return new ReplyTextRequest(null, "", null);
	} 
	
}