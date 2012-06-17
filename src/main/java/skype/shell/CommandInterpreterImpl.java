package skype.shell;

import java.util.Vector;

import skype.ChatAdapterInterface;
import skype.voting.requests.HelpRequest;
import skype.voting.requests.ValidatedShellCommandFactory;
import skype.voting.requests.factories.VotingFactoriesRetriever;

public class CommandInterpreterImpl implements CommandInterpreter {
	final ShellCommandInterpreter[] factories;
	private final AliasProcessor aliasProcessor;
	
	public CommandInterpreterImpl() {
		this(VotingFactoriesRetriever.getFactories());
	}
	
	public CommandInterpreterImpl(ShellCommandInterpreter ...factories) {
		this(VotingFactoriesRetriever.getSingletonAliasProcessor(), factories);
	}

	CommandInterpreterImpl(AliasProcessor aliasProcessor, ShellCommandInterpreter ...factories){
		this.aliasProcessor = aliasProcessor;
		this.factories = makeDecoratedFactories(factories);
	}

	@Override
	public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
		String expandedMessage = aliasProcessor.expandMessage(message);
		if (aliasProcessor.understands(message)){
			return aliasProcessor.processMessage(chat, message);
		}
		
		for (ShellCommandInterpreter aFactory : factories) {
			if (aFactory.understands(expandedMessage)) {
				return aFactory.processMessage(chat, expandedMessage);
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
	
	private static ShellCommandInterpreter[] makeDecoratedFactories(ShellCommandInterpreter... factories) {
		Vector<ShellCommandInterpreter> decorated = new Vector<ShellCommandInterpreter>();
		for (ShellCommandInterpreter inputFactory : factories) {
			decorated.add(new ValidatedShellCommandFactory(inputFactory));
		}
		ShellCommandInterpreter[] array = decorated.toArray(new ShellCommandInterpreter[0]);
		return array;
	}	
}