package skype.voting.requests.factories;

import skype.shell.ShellCommandFactory;
import skype.voting.VotingCommandProcessor;
import skype.voting.processor.abstracts.VotingCommandProcessorAbstract;
import utils.ReflectionUtils;

public class VotingFactoriesRetriever {
	private static final String SKYPE_VOTING_REQUESTS_PACKAGE = VotingFactoriesRetriever.class.getPackage().getName();
	private static final String SKYPE_VOTING_PROCESSORS_PACKAGE = "skype.voting.processors";

	public static ShellCommandFactory[] getFactories() {
		return ReflectionUtils.getInstancesOfClassesInGivenPackage(
				ShellCommandFactory.class,
				SKYPE_VOTING_REQUESTS_PACKAGE);
	}
	
	public static VotingCommandProcessor[] getProcessors() {
		return ReflectionUtils.getInstancesOfClassesInGivenPackage(
				VotingCommandProcessorAbstract.class,
				SKYPE_VOTING_PROCESSORS_PACKAGE);
	}
}