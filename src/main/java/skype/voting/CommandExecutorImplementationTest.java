package skype.voting;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.CommandInterpreter;
import skype.shell.mocks.ChatBridgeMock;

import com.skype.SkypeException;

public class CommandExecutorImplementationTest {
	StringBuilder operations = new StringBuilder();
	
	@Test
	public void onProcessMessage_ShouldInterpretAndSendToProcessor() throws SkypeException
	{
		CommandExecutor commandExecutors = getInterpreterProcessorExecutor();
		commandExecutors.processMessage(new ChatBridgeMock(), null);
		assertEquals(
				"CommandInterpreter processMessage\n" + 
				"ShellCommandExecutor processIfPossible\n", 
				operations.toString());
	}
	
	private CommandExecutor getInterpreterProcessorExecutor() {
		final CommandInterpreter interpreter = getInterpreter();
		final CommandProcessorAdapterMock  processor = getProcessor();
		
		return new CommandExecutorImplementation(interpreter, processor);
	}
	
	private CommandInterpreter getInterpreter() {
		if (interpreterMock != null)
			return interpreterMock;
		
		interpreterMock = new CommandInterpreterMock(operations);
		return interpreterMock;
	}
	
	private CommandProcessorAdapterMock getProcessor() {
		if (processorMock != null)
			return processorMock;
		processorMock = new CommandProcessorAdapterMock(operations);
		return processorMock;
	}
	
	private CommandInterpreter interpreterMock;
	private CommandProcessorAdapterMock processorMock;
}