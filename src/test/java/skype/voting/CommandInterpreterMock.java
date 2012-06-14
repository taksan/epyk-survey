package skype.voting;

import skype.ChatAdapterInterface;
import skype.shell.AbstractShellCommand;
import skype.shell.CommandInterpreter;
import skype.shell.ShellCommand;

final class CommandInterpreterMock implements CommandInterpreter {
	StringBuilder operations;
	public CommandInterpreterMock(StringBuilder op){
		operations = op;
	}
	@Override
	public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
		operations.append("CommandInterpreter processMessage\n");
		return new AbstractShellCommand(chat,message) {
			
		};
	}
}