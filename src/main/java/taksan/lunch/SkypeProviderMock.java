package taksan.lunch;

import taksan.skype.SkypeMessageListener;
import taksan.skype.SkypeMessageProvider;

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
