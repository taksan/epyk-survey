package skype.shell;

import java.util.Vector;

import skype.ChatAdapterInterface;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.ValidatedShellCommandFactory;

public class CommandInterpreterImpl implements CommandInterpreter {
	final ShellCommandFactory[] factories;
	private final AliasProcessor aliasProcessor;
	
	public CommandInterpreterImpl(ShellCommandFactory ...factories) {
		this(new AliasProcessorImpl(), factories);
	}

	CommandInterpreterImpl(AliasProcessor aliasProcessor, ShellCommandFactory ...factories){
		this.aliasProcessor = aliasProcessor;
		this.factories = makeDecoratedFactories(factories);
	}

	@Override
	public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
		String expandedMessage = aliasProcessor.expandMessage(message);
		if (aliasProcessor.understands(message)){
			return aliasProcessor.processMessage(chat, message);
		}
		
		for (ShellCommandFactory aFactory : factories) {
			if (aFactory.understands(expandedMessage)) {
				return aFactory.produce(chat, expandedMessage);
			}
		}
		if (isHelpRequest(expandedMessage)){
			return new HelpRequest(chat,expandedMessage,factories);
		}
		
		return new UnrecognizedCommand(chat, expandedMessage);
	}

	private boolean isHelpRequest(String message) {
		return message.trim().toLowerCase().equals("#help");
	}
	
	private static ShellCommandFactory[] makeDecoratedFactories(ShellCommandFactory... factories) {
		Vector<ShellCommandFactory> decorated = new Vector<ShellCommandFactory>();
		for (ShellCommandFactory inputFactory : factories) {
			decorated.add(new ValidatedShellCommandFactory(inputFactory));
		}
		ShellCommandFactory[] array = decorated.toArray(new ShellCommandFactory[0]);
		return array;
	}	
}