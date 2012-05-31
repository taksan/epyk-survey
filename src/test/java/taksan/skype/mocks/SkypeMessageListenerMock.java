package taksan.skype.mocks;

import taksan.skype.SkypeMessageListener;

public class SkypeMessageListenerMock implements SkypeMessageListener {

	public boolean messageReceived = false;
	public boolean messageSent = false;

	public void onReceivedMessage(String message) {
		messageReceived  = true;
	}

	@Override
	public void onSentMessage(String message) {
		messageSent = true;
	}

}
