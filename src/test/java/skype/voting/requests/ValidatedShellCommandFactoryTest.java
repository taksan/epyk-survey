package skype.voting.requests;

import junit.framework.Assert;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandInterpreter;
import skype.voting.requests.InvalidCommandException;
import skype.voting.requests.ValidatedShellCommandFactory;

public class ValidatedShellCommandFactoryTest {
	@Test
	public void onParseOfCommandThatDecoratedDoesntUnderstand_ShouldThrowException()
	{
		ShellCommandInterpreter factory = new ShellCommandInterpreter() {
			@Override
			public boolean understands(String message) {
				return false;
			}
			
			@Override
			public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}

			@Override
			public String getHelp() {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
		};
		ValidatedShellCommandFactory subject = new ValidatedShellCommandFactory(factory);
		try {
			subject.processMessage(null, "#3@");
			Assert.fail("Should not accept malformed message");
		}catch(InvalidCommandException e) {
			Assert.assertEquals("Invalid command #3@", e.getMessage());
		}
	}
}