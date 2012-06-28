package voting.main;

import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.connector.Connector;

public class EpykSurveyMain {
	public static void main(String[] args) throws SkypeException {
		System.setProperty("skype.api.impl", "dbus");
		if (!Skype.isRunning()) {
			System.out.println("Skype must be running to start this application");
			System.exit(-1);
		}
		
		Connector.getInstance().setApplicationName("EpykSurvey");
		Skype.setDebug(true);
		Skype.setDebugNative(true);
		Skype.setDaemon(false);
		new EpykSurveyApp().execute();
	}
}
