package skype.shell;

import skype.ChatAdapterInterface;


public interface AliasProcessor {

	String expandMessage(String message);

	boolean understands(String message);

	ShellCommand processMessage(ChatAdapterInterface chat, String message);

}
