package skype.shell;

import skype.lunch.LunchRequest;

public interface ShellCommandVisitor {

	void processSentRequest(LunchRequest lunchRequest);

}
