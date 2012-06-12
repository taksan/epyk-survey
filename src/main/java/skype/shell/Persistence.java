package skype.shell;

import java.util.Map;

public interface Persistence {
	public Map<String, String> loadAliases();
	public void saveAliases(Map<String, String> data);
}
