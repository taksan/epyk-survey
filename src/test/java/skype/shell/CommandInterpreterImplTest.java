package skype.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.lunch.UnrecognizedCommand;
import skype.shell.mocks.ShellCommandFactoryMock;


public class CommandInterpreterImplTest {
	@Test
	public void onUnrecognizedPattern_ShouldReturnUnrecognizedCommand() {
		CommandInterpreterImpl subject = new CommandInterpreterImpl();
		ShellCommand shellCommand = subject.processMessage("#42", "#foo");
		assertTrue(shellCommand instanceof UnrecognizedCommand);
	}
	
	@Test
	public void onRecognizedCommand_ShouldProduceGivenCommand() {
		ShellCommandFactory cmd1 = new ShellCommandFactoryMock();
		CommandInterpreterImpl subject = new CommandInterpreterImpl(cmd1);
		ShellCommand shellCommand = subject.processMessage("#42", "#understood_command");
		assertEquals("Understood", shellCommand.getText());
	}
	
}