package skype.shell;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import skype.voting.processors.interpreters.VotingFactoriesRetriever;

public class VotingFactoriesRetrieverTest {
	@Test
	public void onGetFactories_ShouldReturnAtLeastOneFactory() {
		ShellCommandInterpreter[] factories = VotingFactoriesRetriever.getFactories();
		assertTrue(factories.length > 0);
		
		for (ShellCommandInterpreter factory : factories) {
			factory.getHelp();
		}
	}
}