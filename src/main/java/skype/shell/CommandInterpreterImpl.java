package skype.shell;

import java.util.Vector;

import skype.ChatAdapterInterface;
import skype.voting.requests.ValidatedShellCommandFactory;

public class CommandInterpreterImpl implements CommandInterpreter {
	final ShellCommandFactory[] factories;

	public CommandInterpreterImpl(ShellCommandFactory ...factories){
		Vector<ShellCommandFactory> decorated = new Vector<ShellCommandFactory>();
		for (ShellCommandFactory inputFactory : factories) {
			decorated.add(new ValidatedShellCommandFactory(inputFactory));
		}
		this.factories = decorated.toArray(new ShellCommandFactory[0]);
	}

	@Override
	public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
		for (ShellCommandFactory aFactory : factories) {
			if (aFactory.understands(message)) {
				return aFactory.produce(chat, message);
			}
		}
		
		return new UnrecognizedCommand(chat, message);
	}
}
