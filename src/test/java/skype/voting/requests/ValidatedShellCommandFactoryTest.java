package skype.voting.requests;

import junit.framework.Assert;

import org.junit.Test;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandFactory;
import skype.voting.requests.InvalidCommandException;
import skype.voting.requests.ValidatedShellCommandFactory;

public class ValidatedShellCommandFactoryTest {
	@Test
	public void onParseOfCommandThatDecoratedDoesntUnderstand_ShouldThrowException()
	{
		ShellCommandFactory factory = new ShellCommandFactory() {
			@Override
			public boolean understands(String message) {
				return false;
			}
			
			@Override
			public ShellCommand produce(ChatAdapterInterface chat, String message) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}

			@Override
			public String getHelp() {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
		};
		ValidatedShellCommandFactory subject = new ValidatedShellCommandFactory(factory);
		try {
			subject.produce(null, "#3@");
			Assert.fail("Should not accept malformed message");
		}catch(InvalidCommandException e) {
			Assert.assertEquals("Invalid command #3@", e.getMessage());
		}
	}
}