package voting.main;

import com.skype.Skype;
import com.skype.SkypeException;

public class VotingTimeMain {
	public static void main(String[] args) throws SkypeException {
		System.setProperty("skype.api.impl", "dbus");
		if (!Skype.isRunning()) {
			System.out.println("Skype must be running to start this application");
			System.exit(-1);
		}
		Skype.setDebug(true);
		Skype.setDaemon(false);
		new VotingTimeApp().execute();
	}
}
