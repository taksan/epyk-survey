package taksan.lunch.main;

import com.skype.Skype;
import com.skype.SkypeException;

public class LunchMain {
	public static void main(String[] args) throws SkypeException {
		if (!Skype.isRunning()) {
			System.out.println("Skype must be running to start this application");
			System.exit(-1);
		}
		
		ChatMessageListenerImplementation listener = new ChatMessageListenerImplementation();
		Skype.addChatMessageListener(listener);
	}
}
