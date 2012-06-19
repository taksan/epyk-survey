package skype.voting.mocks;

import java.util.Map;

import skype.alias.AliasExpander;
import skype.shell.RemoveAliasFeedbackHandler;

public class CopyAliasExpander implements AliasExpander {

	@Override
	public String expand(String message) {
		return message;
	}

	@Override
	public void createNewAlias(String alias, String expansion) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public Map<String, String> getAliases() {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public void removeAlias(String alias, RemoveAliasFeedbackHandler feedback) {
		throw new RuntimeException("NOT IMPLEMENTED");
	}

}
