package skype.voting.processor.abstracts;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.alias.mocks.HelpVisitorMock;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandInterpreter;
import skype.shell.mocks.ShellCommandFactoryMock;

public class VotingCommandProcessorAbstractTest {
	@Test
	public void onAcceptHelpVisitor_ShouldInvokeOnTopicWithInterpreterGetHelp()
	{
		final ShellCommandInterpreter interpreter = new ShellCommandFactoryMock();
		class Subject extends VotingCommandProcessorAbstract{

			protected Subject() {
				super(interpreter);
			}

			@Override
			public void process(ShellCommand command) {
				throw new RuntimeException("NOT IMPLEMENTED");
			}
			
		}
		StringBuilder operations = new StringBuilder();
		new Subject().acceptHelpVisitor(new HelpVisitorMock(operations));
		assertEquals("onTopic: ShellCommandFactoryMock help\n", operations.toString());
	}
}