package taksan.skype;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import taksan.skype.mocks.SkypeMessageListenerMock;

import com.skype.SkypeException;

public class SkypeMessageProviderImplTest { 
	SkypeMessageProviderImpl subject = new SkypeMessageProviderImpl();
	private SkypeMessageListenerMock listener1;
	private SkypeMessageListenerMock listener2;
	
	public SkypeMessageProviderImplTest() {
		listener1 = new SkypeMessageListenerMock();
		listener2 = new SkypeMessageListenerMock();
		subject.addMessageListener(listener1);
		subject.addMessageListener(listener2);
	}
	
	@Test
	public void onReceivedChatMessage_ShouldNotifyAllListeners() throws SkypeException {
		subject.chatMessageReceived(null);
		assertTrue(listener1.messageReceived && listener2.messageReceived);
	}
	
	@Test
	public void onSendChatMessage_ShouldNotifyAllListeners() throws SkypeException {
		subject.chatMessageSent(null);
		assertTrue(listener1.messageSent && listener2.messageSent);
	}
}