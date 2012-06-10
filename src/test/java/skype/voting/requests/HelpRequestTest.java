package skype.voting.requests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.ShellCommand;
import skype.shell.ShellCommandHelper;

public class HelpRequestTest extends RequestTest {
	@Test
	public void onAcceptProcessor_ShouldPrintHelpForAllInputFactories()
	{
		ShellCommandHelper fac1 = new ShellCommandHelper() {
			@Override
			public String getHelp() {
				return "myHelp";
			}
		};
		ShellCommandHelper fac2 = new ShellCommandHelper() {
			@Override
			public String getHelp() {
				return "anotherhelp";
			}
		};
		
		HelpRequest subject = new HelpRequest(fac1,fac2);
		String actual = subject.getHelpMessage();
		String expected = 
				"Available commands:\n" +
				"Command: myHelp\n" + 
				"Command: anotherhelp";
		assertEquals(expected, actual);
	}

	@Override
	protected ShellCommand getSubject() {
		return new HelpRequest();
	}
}