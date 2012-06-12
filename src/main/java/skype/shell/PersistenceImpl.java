package skype.shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class PersistenceImpl implements Persistence {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> loadAliases() {
		File baseDir = getBaseDir();
		File aliases = new File(baseDir, "aliases");
		if (!aliases.exists())
			return new LinkedHashMap<String, String>();
		
		Map<String, String> aliasMap;
		ObjectInputStream stream = null;
		try {
			stream = new ObjectInputStream(new FileInputStream(aliases));
			aliasMap = (Map<String, String>) stream.readObject();
			return aliasMap;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		finally {
			if (stream != null)
			try {
				stream.close();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	@Override
	public void saveAliases(Map<String, String> data) {
		File baseDir = getBaseDir();
		File aliases = new File(baseDir, "aliases");
		
		ObjectOutputStream stream = null;
		try {
			stream = new ObjectOutputStream(new FileOutputStream(aliases));
			stream.writeObject(data);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
		finally {
			if (stream != null)
			try {
				stream.close();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
	}

	private File getBaseDir() {
		String home = System.getProperty("user.home");
		File basedir = new File(home,".skypevoting");
		if (!basedir.exists())
			basedir.mkdir();
		return basedir;
	}
}
