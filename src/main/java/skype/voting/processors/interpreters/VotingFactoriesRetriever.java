package skype.voting.processors.interpreters;

import skype.alias.AliasCommandExecutor;
import skype.alias.AliasExpander;
import skype.alias.AliasExpanderImpl;
import skype.shell.PersistenceImpl;
import skype.shell.ShellCommandInterpreter;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import utils.ReflectionUtils;

public class VotingFactoriesRetriever {
	private static final String SKYPE_VOTING_REQUESTS_PACKAGE = VotingFactoriesRetriever.class.getPackage().getName();
	private static final String SKYPE_VOTING_PROCESSORS_PACKAGE = "skype.voting.processors";
	private static AliasExpander aliasExpander = new AliasExpanderImpl(new PersistenceImpl());

	public static ShellCommandInterpreter[] getFactories() {
		return ReflectionUtils.getInstancesOfClassesInGivenPackage(
				ShellCommandInterpreter.class,
				SKYPE_VOTING_REQUESTS_PACKAGE);
	}
	
	public static VotingCommandProcessorAbstract[] getProcessors() {
		VotingCommandProcessorAbstract[] instancesOfClassesInGivenPackage = ReflectionUtils.getInstancesOfClassesInGivenPackage(
				VotingCommandProcessorAbstract.class,
				SKYPE_VOTING_PROCESSORS_PACKAGE);
		return instancesOfClassesInGivenPackage;
	}

	final static AliasCommandExecutor aliasProcessorSingleton = new AliasCommandExecutor(new PersistenceImpl());
	public static AliasCommandExecutor getSingletonAliasProcessor() {
		return aliasProcessorSingleton;
	}

	public static AliasExpander getSingletonAliasExpander() {
		return aliasExpander;
	}
}