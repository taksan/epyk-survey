package skype.shell;
	
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.shell.mocks.AliasProcessorMock;
import skype.shell.mocks.ShellCommandFactoryMock;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.ValidatedShellCommandFactory;


public class CommandInterpreterImplTest {
	private ShellCommandInterpreter cmd1 = new ShellCommandFactoryMock();
	private AliasProcessorMock mock = new AliasProcessorMock();
	private CommandInterpreterImpl subject = new CommandInterpreterImpl(mock,cmd1);
	
	@Test
	public void onConstruction_ShouldDecorateFactoriesWithValidatorDecorator()
	{
		assertTrue("Internal factories must be decorated by ValidatedShellCommandFactory",
				subject.factories[0] instanceof ValidatedShellCommandFactory);
	}
	
	@Test
	public void onHelpCommand_ShouldReturnHelpRequest(){
		ShellCommand shellCommand = subject.processMessage(null, "#help");
		assertTrue(shellCommand instanceof HelpRequest);
	}
	
	@Test
	public void onUnrecognizedPattern_ShouldReturnUnrecognizedCommand() {
		ShellCommand shellCommand = subject.processMessage(null, "#foo");
		assertTrue(shellCommand instanceof UnrecognizedCommand);
	}
	
	@Test
	public void onRecognizedCommand_ShouldProduceGivenCommand() {
		ShellCommand shellCommand = subject.processMessage(null, "#understood_command");
		assertEquals("Understood", shellCommand.getText());
	}
	
	@Test
	public void onAnyMessage_ShouldBeDelegatedToAliasProcessorIfItUnderstands()
	{
		mock.setUnderstands("#foo");
		subject.processMessage(null, "#foo");
		assertEquals(
				"expandMessage:#foo\n" + 
				"understands:#foo\n" + 
				"processMessage:#foo\n",
				mock.getOperations());
	}
	
	@Test
	public void onAliasedMessage_ShouldDelegateExpandedMessageToAppropriateFactory(){
		mock.setAlias("#foo","#understood_command");
		ShellCommand shellCommand = subject.processMessage(null, "#foo");
		assertEquals("Understood", shellCommand.getText());		
	}
}