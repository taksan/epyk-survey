package skype.voting.requests.factories;

import skype.shell.ShellCommandInterpreter;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import utils.ReflectionUtils;

public class VotingFactoriesRetriever {
	private static final String SKYPE_VOTING_REQUESTS_PACKAGE = VotingFactoriesRetriever.class.getPackage().getName();
	private static final String SKYPE_VOTING_PROCESSORS_PACKAGE = "skype.voting.processors";

	public static ShellCommandInterpreter[] getFactories() {
		return ReflectionUtils.getInstancesOfClassesInGivenPackage(
				ShellCommandInterpreter.class,
				SKYPE_VOTING_REQUESTS_PACKAGE);
	}
	
	public static VotingCommandProcessorAbstract[] getProcessors() {
		return ReflectionUtils.getInstancesOfClassesInGivenPackage(
				VotingCommandProcessorAbstract.class,
				SKYPE_VOTING_PROCESSORS_PACKAGE);
	}
}