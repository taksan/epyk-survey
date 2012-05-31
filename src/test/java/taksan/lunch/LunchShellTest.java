package taksan.lunch;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import taksan.skype.shell.CommandInterpreter;
import taksan.skype.shell.ShellCommand;

public class LunchShellTest {
	final AtomicReference<Boolean> messageProcessed = new AtomicReference<Boolean>(false);
	CommandInterpreter commandInterpreter = new CommandInterpreterMock(messageProcessed);
	private final class CommandInterpreterMock implements
			CommandInterpreter {
		private final AtomicReference<Boolean> messageProcessed;

		private CommandInterpreterMock(
				AtomicReference<Boolean> messageProcessed) {
			this.messageProcessed = messageProcessed;
		}

		public ShellCommand processMessage(String message) {
			messageProcessed.set(true);
			return null;
		}
	}

	SkypeProviderMock skypeProvider = new SkypeProviderMock();
	LunchShell subject = new LunchShell(skypeProvider, commandInterpreter );
	
	@Test
	public void onMessageReceived_ShouldProcessNotification() {
		skypeProvider.messageReceived("foo");
		assertTrue(messageProcessed.get());
	}
	
	@Test
	public void onMessageSent_ShouldProcessNotification() {
		skypeProvider.messageSent("foo");
		assertTrue(messageProcessed.get());
	}
}
