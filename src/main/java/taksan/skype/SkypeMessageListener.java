package taksan.skype;

public interface SkypeMessageListener {
	void onReceivedMessage(String message);

	void onSentMessage(String message);
}
