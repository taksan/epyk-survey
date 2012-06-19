package skype.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.shell.mocks.AliasExpanderMock;

public class AliasProcessorImplTest { 
	private PersistenceMock persistence = new PersistenceMock();
	AliasExpanderMock expander = new AliasExpanderMock(persistence);
	AliasProcessorImpl subject = new AliasProcessorImpl(expander);
	
	@Test
	public void onAliasCommand_ShouldGenerateAReplyTextRequest() {
		String message = "#alias foo #startpoll \"some poll\"";
		assertTrue(subject.understands(message));
		
		ReplyTextRequest aliasRequest = (ReplyTextRequest) subject.processMessage(null, message);
		
		assertTrue(expander.createNewAliasInvoked());
		
		String reply = aliasRequest.getReplyText();
		assertEquals("Alias '#foo' created to expand to <#startpoll \"some poll\">", reply);
	}
	
	@Test
	public void onListAliasCommand_ShouldGenerateReplyTextMessageWithListOfAliases(){
		persistence.addAlias("foo1", "expanded1");
		persistence.addAlias("foo2", "expanded2");
		
		ReplyTextRequest aliasRequest = (ReplyTextRequest) subject.processMessage(null, "#aliaslist");
		assertTrue(expander.getAliasesInvoked());
		String reply = aliasRequest.getReplyText();
		assertEquals(
				"Registered aliases:\n" +
				"#foo1 : expanded1\n" +
				"#foo2 : expanded2", 
				reply);
	}
	
	@Test
	public void onRemoveAliasCommand_ShouldRemoveTheAlias() {
		persistence.setSaveInvokedFalse();
		
		ReplyTextRequest removeRequest = (ReplyTextRequest) subject.processMessage(null, "#aliasdel foo1");
		assertTrue(expander.removeAliasInvoked());
		
		String reply2 = removeRequest.getReplyText();
		assertEquals("Alias 'foo1' removed.", reply2);
	}
	
	@Test
	public void onRemoveAliasOnNonExistingAlias_ShouldReplyAliasDoesNotExist()
	{
		expander.setFailOnRemoval();
		ReplyTextRequest removeRequest = (ReplyTextRequest) subject.processMessage(null, "#aliasdel foo1");
		String reply = removeRequest.getReplyText();
		assertEquals("Alias 'foo1' doesn't exist.", reply);
	}
	
}