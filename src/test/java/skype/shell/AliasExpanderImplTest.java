package skype.shell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

public class AliasExpanderImplTest { 
	PersitenceMock persistence = new PersitenceMock();

	
	@Test
	public void onCreate_ShouldRestoreAliasFromPersistance()
	{
		new AliasExpanderImpl(persistence);
		assertTrue(persistence.loadAliasesInvoked());
	}
	
	@Test
	public void onMessageThatMatchesAlias_ShouldExpandAlias(){
		persistence.addAlias("foo", "#startpoll \"some poll\"");
		
		AliasExpanderImpl subject = new AliasExpanderImpl(persistence);
		
		String expandMessage = subject.expand("#foo");
		
		assertEquals("#startpoll \"some poll\"", expandMessage);
	}
	
	@Test
	public void onMessageThatMatchesAliasAndHasArguments_ShouldExpandAliasWithArguments()
	{
		persistence.addAlias("foo", "#startpoll \"some poll\"");
		AliasExpanderImpl subject = new AliasExpanderImpl(persistence);
		
		String expandMessage = subject.expand("#foo 123");
		assertEquals("#startpoll \"some poll\" 123", expandMessage);
	}

	@Test
	public void onRemoveAliasCommand_ShouldRemoveTheAlias() {
		persistence.addAlias("foo", "#startpoll \"some poll\"");
		
		AliasExpanderImpl subject = new AliasExpanderImpl(persistence);
		
		final AtomicBoolean removedSucessInvoked = new AtomicBoolean(false);
		subject.removeAlias("#foo", new RemoveAliasFeedbackHandler() {
			
			@Override
			public void onSuccess() {
				removedSucessInvoked.set(true);
			}
			
			@Override
			public void onAliasDoesNotExist() {
				throw new RuntimeException("Should never get here");
			}
		});
		assertTrue(removedSucessInvoked.get());
		assertTrue(persistence.saveAliasesInvoked());
	}
}