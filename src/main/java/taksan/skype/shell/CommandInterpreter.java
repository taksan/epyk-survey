package taksan.skype.shell;

public interface CommandInterpreter {
	public ShellCommand processMessage(String message);
}
