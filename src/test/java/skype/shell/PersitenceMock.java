package skype.shell;

import java.util.LinkedHashMap;
import java.util.Map;

public class PersitenceMock implements Persistence {

	private boolean loadInvoked;
	private boolean saveInvoked;

	@Override
	public Map<String, String> loadAliases() {
		loadInvoked = true;
		return new LinkedHashMap<String, String>();
	}
	
	@Override
	public void saveAliases(Map<String,String> data) {
		saveInvoked = true;
	}
	
	public boolean loadAliasesInvoked() {
		return loadInvoked;
	}

	public boolean saveAliasesInvoked() {
		return saveInvoked;
	}

	public void setSaveInvokedFalse() {
		saveInvoked = false;
	}


}
