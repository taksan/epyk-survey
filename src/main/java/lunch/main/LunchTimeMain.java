package lunch.main;

import com.skype.Skype;
import com.skype.SkypeException;

public class LunchTimeMain {
	public static void main(String[] args) throws SkypeException {
		System.setProperty("skype.api.impl", "dbus");
		if (!Skype.isRunning()) {
			System.out.println("Skype must be running to start this application");
			System.exit(-1);
		}
		Skype.setDaemon(false);
		new LunchTimeApp().execute();
	}
}
