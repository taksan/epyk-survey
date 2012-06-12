package skype.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AliasProcessorImplTest { 
	private PersitenceMock persistence = new PersitenceMock();
	AliasProcessorImpl subject = new AliasProcessorImpl(persistence);
	

	@Test
	public void onCreate_ShouldRestoreAliasFromPersistance()
	{
		assertTrue(persistence.loadAliasesInvoked());
	}
	
	@Test
	public void onAliasCommand_ShouldGenerateAReplyTextRequest() {
		String message = "#alias foo #startpoll \"some poll\"";
		
		assertTrue(subject.understands(message));
		
		ReplyTextRequest aliasRequest = (ReplyTextRequest) subject.processMessage(null, message);
		String reply = aliasRequest.getReplyText();
		assertEquals("Alias '#foo' created to expand to <#startpoll \"some poll\">", reply);
		
		assertTrue(persistence.saveAliasesInvoked());
	}
	
	@Test
	public void onMessageThatMatchesAlias_ShouldExpandAlias(){
		String message = "#alias foo #startpoll \"some poll\"";
		subject.processMessage(null, message);
		
		String expandMessage = subject.expandMessage("#foo");
		assertEquals("#startpoll \"some poll\"", expandMessage);
	}
	
	@Test
	public void onMessageThatMatchesAliasAndHasArguments_ShouldExpandAliasWithArguments()
	{
		String message = "#alias foo #startpoll \"some poll\"";
		subject.processMessage(null, message);
		String expandMessage = subject.expandMessage("#foo 123");
		assertEquals("#startpoll \"some poll\" 123", expandMessage);
	}
	
	@Test
	public void onListAliasCommand_ShouldGenerateReplyTextMessageWithListOfAliases(){
		subject.processMessage(null, "#alias foo1 expanded1");
		subject.processMessage(null, "#alias foo2 expanded2");
		ReplyTextRequest aliasRequest = (ReplyTextRequest) subject.processMessage(null, "#aliaslist");
		String reply = aliasRequest.getReplyText();
		assertEquals(
				"Registered aliases:\n" +
				"#foo1 : expanded1\n" +
				"#foo2 : expanded2", 
				reply);
	}
	
	@Test
	public void onRemoveAliasCommand_ShouldRemoveTheAlias() {
		subject.processMessage(null, "#alias foo1 expanded1");
		subject.processMessage(null, "#alias foo2 expanded2");
		persistence.setSaveInvokedFalse();
		
		ReplyTextRequest removeRequest = (ReplyTextRequest) subject.processMessage(null, "#aliasdel foo1");
		String reply = removeRequest.getReplyText();
		assertEquals("Alias 'foo1' removed.", reply);
		ReplyTextRequest aliasRequest = (ReplyTextRequest) subject.processMessage(null, "#aliaslist");
		String reply2 = aliasRequest.getReplyText();
		assertEquals(
				"Registered aliases:\n" +
				"#foo2 : expanded2", 
				reply2);
		assertTrue(persistence.saveAliasesInvoked());
	}
	
	@Test
	public void onRemoveAliasOnNonExistingAlias_ShouldReplyAliasDoesNotExist()
	{
		ReplyTextRequest removeRequest = (ReplyTextRequest) subject.processMessage(null, "#aliasdel foo1");
		String reply = removeRequest.getReplyText();
		assertEquals("Alias 'foo1' doesn't exist.", reply);
	}
	
}