package skype.shell;

import java.util.Map;

public class AliasExpanderImpl implements AliasExpander {
	
	final private Map<String, String> aliases;
	private final Persistence persistence;
	public AliasExpanderImpl(Persistence persistence) {
		this.persistence = persistence;
		this.aliases = persistence.loadAliases();
	}

	@Override
	public String expand(String message) {
		String candidateAlias = message.replaceAll("(#[^ ]*)\\s.*", "$1");
		String expanded = aliases.get(candidateAlias);
		if (expanded == null)
			return message;
		String arguments = message.replace(candidateAlias, "");
		return expanded+arguments;
	}

	@Override
	public void createNewAlias(String alias, String expanded) {
		aliases.put(alias, expanded);
		persistence.saveAliases(aliases);
	}

	@Override
	public Map<String, String> getAliases() {
		return aliases;
	}

	@Override
	public void removeAlias(String aliasKey, RemoveAliasFeedbackHandler feedback) {
		if (aliases.containsKey(aliasKey)){
			aliases.remove(aliasKey);
			persistence.saveAliases(aliases);
			
			feedback.onSuccess();
		}
		else {
			feedback.onAliasDoesNotExist();
		}
	}


}
