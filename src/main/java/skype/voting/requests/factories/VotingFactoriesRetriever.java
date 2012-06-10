package skype.voting.requests.factories;

import skype.shell.ShellCommandFactory;
import utils.ReflectionUtils;

public class VotingFactoriesRetriever {
	private static final String SKYPE_VOTING_REQUESTS_PACKAGE = VotingFactoriesRetriever.class.getPackage().getName();

	public static ShellCommandFactory[] getFactories() {
		return ReflectionUtils.getInstancesOfClassesInGivenPackage(
				ShellCommandFactory.class,
				SKYPE_VOTING_REQUESTS_PACKAGE);
	}
}