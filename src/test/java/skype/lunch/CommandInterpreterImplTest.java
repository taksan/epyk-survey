package skype.lunch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.ChatId;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandVisitor;


public class CommandInterpreterImplTest {
	@Test
	public void onRecognizedCommand_ShouldProduceGivenCommand() {
		ShellCommandFactory cmd1 = new ShellCommandFactory() {
			
			@Override
			public ShellCommand produce(final ChatId chatId, final String message) {
				if (!understands(message))
					return new UnrecognizedCommand(null, message);
				
				return new ShellCommand() {
						@Override public String getText() {
							return "Understood";
						}
						
						@Override public void acceptSentRequest(ShellCommandVisitor visitor) {
							throw new RuntimeException("NOT IMPLEMENTED");
						}
				};
			}

			private boolean understands(String message) {
				return message.startsWith("#understood_command");
			}
		};
		CommandInterpreterImpl subject = new CommandInterpreterImpl(cmd1);
		ShellCommand shellCommand = subject.processMessage("#42", "#understood_command");
		assertEquals("Understood", shellCommand.getText());
	}
}