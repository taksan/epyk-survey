package skype.alias;

import java.util.Map;


public interface AliasExpander {
	public String expand(String message);
	public void createNewAlias(String alias, String expansion);
	public Map<String,String> getAliases();
	public void removeAlias(String alias, RemoveAliasFeedbackHandler feedback);
}
