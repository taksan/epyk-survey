package skype.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.alias.HelpProvider;
import skype.shell.mocks.ChatBridgeMock;
import skype.voting.HelpVisitor;
import skype.voting.ReplyListenerMock;

public class HelpCommandExecutorTest {
	@Test
	public void onRequestsThatAreNotHelp_ShouldReturnFalse()
	{
		HelpCommandExecutor subject = new HelpCommandExecutor();
		assertFalse(subject.processMessage(null, "#ffo"));
	}
	
	@Test
	public void onHelpRequest_ShouldVisitHelpProvidersAndPrintTheirMessages(){
		HelpProvider [] helpers = new HelpProvider[]{
				new HelpProvider() {
					@Override
					public void acceptHelpVisitor(HelpVisitor helpVisitor) {
						helpVisitor.
							onTopLevel("TopLevel 1").
								onTopic("Topic 1.1").
									onTopicDescription("1.1 description").
								onTopic("Topic 1.2").
									onTopicDescription("1.2 description");
					}
				},
				new HelpProvider() {
					@Override
					public void acceptHelpVisitor(HelpVisitor helpVisitor) {
						helpVisitor.
							onTopLevel("TopLevel 2").
								onTopic("Topic 2.1").
									onTopicDescription("2.1 description").
								onTopic("Topic 2.2").
									onTopicDescription("2.2 description").
								onTopic("Topic 2.3 - inline description");
					}
				}
		};
		
		HelpCommandExecutor subject = new HelpCommandExecutor(helpers);
		ChatBridgeMock bridgeMock = new ChatBridgeMock(null, "john doe");
		ReplyListenerMock listener = new ReplyListenerMock();
		subject.setReplyListener(listener );
		assertTrue(subject.processMessage(bridgeMock, "#help"));
		assertEquals("HELP.\n" +
				"* TopLevel 1\n" +
				"    Topic 1.1\n" +
				"        1.1 description\n" +
				"    Topic 1.2\n" +
				"        1.2 description\n" +
				"* TopLevel 2\n" +
				"    Topic 2.1\n" +
				"        2.1 description\n" +
				"    Topic 2.2\n" +
				"        2.2 description\n"+
				"    Topic 2.3 - inline description\n" 
				, listener.replyPrivate.get());
	}
}