package skype.shell;

public interface CommandInterpreter {
	public ShellCommand processMessage(String chatId, String message);
}
