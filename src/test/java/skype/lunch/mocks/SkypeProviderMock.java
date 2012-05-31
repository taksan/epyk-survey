package skype.lunch.mocks;

import skype.SkypeMessageListener;
import skype.SkypeMessageProvider;

public class SkypeProviderMock implements SkypeMessageProvider {
	
	private SkypeMessageListener listener;

	public void addMessageListener(SkypeMessageListener listener) {
		this.listener = listener;
	}

	public void messageReceived(String message) {
		listener.onReceivedMessage(message);
	}

	public void messageSent(String message) {
		listener.onSentMessage(message);
	}

}
