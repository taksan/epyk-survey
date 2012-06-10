package skype.shell;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.voting.requests.factories.VotingFactoriesRetriever;

public class VotingFactoriesRetrieverTest {
	@Test
	public void onGetFactories_ShouldReturnAtLeastOneFactory() {
		ShellCommandFactory[] factories = VotingFactoriesRetriever.getFactories();
		assertTrue(factories.length > 0);
		
		for (ShellCommandFactory factory : factories) {
			factory.getHelp();
		}
	}
}