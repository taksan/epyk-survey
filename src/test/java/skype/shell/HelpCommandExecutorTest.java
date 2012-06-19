package skype.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;
import skype.voting.ReplyListenerMock;

public class HelpCommandExecutorTest { 
	@Test
	public void onRequestsThatAreNotHelp_ShouldReturnFalse()
	{
		HelpCommandExecutor subject = new HelpCommandExecutor();
		assertFalse(subject.processMessage(null, "#ffo"));
	}
	
	@Test
	public void onHelpRequest_ShouldPrintHelpForAllInputProcessorsHelp() {
		ShellCommandHelper one = new ShellCommandHelper() {
			public String getHelp() {
				return "#help 1";
			}
		};
		ShellCommandHelper two = new ShellCommandHelper() {
			public String getHelp() {
				return "#help 2";
			}
		};
		
		ShellCommandHelper [] helpers = new ShellCommandHelper []{one, two};
		
		HelpCommandExecutor subject = new HelpCommandExecutor(helpers);
		ChatBridgeMock bridgeMock = new ChatBridgeMock(null, "john doe");
		ReplyListenerMock listener = new ReplyListenerMock();
		subject.setReplyListener(listener );
		assertTrue(subject.processMessage(bridgeMock, "#help"));
		
		assertEquals("------------------\n" +
				"Available commands\n" +
				"- #help 1\n" +
				"- #help 2\n", listener.replyPrivate.get());
	}
}