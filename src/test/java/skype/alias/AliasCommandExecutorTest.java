package skype.alias;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.alias.AliasCommandExecutor;
import skype.alias.mocks.HelpVisitorMock;
import skype.shell.PersistenceMock;
import skype.shell.mocks.AliasExpanderMock;
import skype.voting.HelpVisitor;
import skype.voting.ReplyListenerMock;

public class AliasCommandExecutorTest { 
	private PersistenceMock persistence = new PersistenceMock();
	AliasExpanderMock expander = new AliasExpanderMock(persistence);
	AliasCommandExecutor subject = new AliasCommandExecutor(expander);
	ReplyListenerMock listenerMock = new ReplyListenerMock();
	
	public AliasCommandExecutorTest() {
		subject.setReplyListener(listenerMock);
	}
	
	@Test
	public void onProcessAliasMessage_ShoulInvokeExpanderCreateAliasAndGenerateReply()
	{
		String message = "#alias foo #startpoll \"some poll\"";
		subject.processMessage(null, message);
		assertTrue(expander.createNewAliasInvoked());
		
		String reply = listenerMock.reply.get();
		assertEquals("Alias '#foo' created to expand to <#startpoll \"some poll\">", reply);
	}
	
	@Test
	public void onListAliasCommand_ShouldGenerateReplyTextMessageWithListOfAliases(){
		persistence.addAlias("foo1", "expanded1");
		persistence.addAlias("foo2", "expanded2");
		
		subject.processMessage(null, "#aliaslist");
		assertTrue(expander.getAliasesInvoked());
		String reply = listenerMock.reply.get();
		assertEquals(
				"Registered aliases:\n" +
				"#foo1 : expanded1\n" +
				"#foo2 : expanded2", 
				reply);
	}
	
	@Test
	public void onRemoveAliasCommand_ShouldRemoveTheAlias() {
		persistence.setSaveInvokedFalse();
		
		subject.processMessage(null, "#aliasdel foo1");
		assertTrue(expander.removeAliasInvoked());
		
		String reply = listenerMock.reply.get();
		assertEquals("Alias 'foo1' removed.", reply);
	}
	
	@Test
	public void onRemoveAliasOnNonExistingAlias_ShouldReplyAliasDoesNotExist()
	{
		expander.setFailOnRemoval();
		subject.processMessage(null, "#aliasdel foo1");
		String reply = listenerMock.reply.get();
		assertEquals("Alias 'foo1' doesn't exist.", reply);
	}

	@Test
	public void onProcessUnknownMessage_ShouldReturnFalse(){
		assertFalse(subject.processMessage(null, "#foo"));
	}
	
	@Test
	public void onAcceptHelpVisitor_ShouldVisitHelpMessages()
	{
		StringBuilder operations = new StringBuilder();
		HelpVisitor helpVisitor = new HelpVisitorMock(operations);
		subject.acceptHelpVisitor(helpVisitor );
		String expectedAliasHelp = "onTopLevel: Alias manipulation:\n" +
				"onTopic: #alias <aliasname> <expansion>\n"+
				"onTopicDescription: creates a new <alias> that will expand to the given text starting when #aliasname is typed\n" +
				"onTopic: #aliaslist\n" + 
				"onTopicDescription: display the existing aliases\n" +
				"onTopic: #aliasdel <aliasname>\n" +
				"onTopicDescription: removes specified alias\n";
				
		
		String expectedWithoutAlias = expectedAliasHelp+
				"onTopLevel: No aliases have been defined yet\n";;
		assertEquals(expectedWithoutAlias, operations.toString());
		operations.setLength(0);
		
		persistence.addAlias("foo1", "expanded1");
		persistence.addAlias("foo2", "expanded2");
		subject.acceptHelpVisitor(helpVisitor);
		
		String expected = expectedAliasHelp	 +
				"onTopLevel: Currently defined aliases\n"+
				"onTopic: alias #foo1 : expanded1\n"+
				"onTopic: alias #foo2 : expanded2\n";
				
		
		assertEquals(expected, operations.toString());
	}
}