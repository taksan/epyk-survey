package skype.shell;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import skype.ChatAdapterInterface;

public class AliasProcessorImpl implements AliasProcessor {
	final private Map<String, String> aliases = new LinkedHashMap<String, String>();	

	@Override
	public boolean understands(String message) {
		return isAddAlias(message) || isListAlias(message) || isRemoveAlias(message);
	}
	
	@Override
	public String expandMessage(String message) {
		return expandMessageIfNeeded(message);
	}
	
	@Override
	public ShellCommand processMessage(ChatAdapterInterface chat, String message) {
		if (!understands(message))
			return null;
		
		if (isAddAlias(message))
			return processAliasRequestAndGenerateReply(chat, message);
		if (isListAlias(message))
			return processListAliasRequest(chat, message);
		if (isRemoveAlias(message))
			return processRemoveAliasRequest(chat, message);
		
		return processAliasRequestAndGenerateReply(chat, message);
	}


	private boolean isRemoveAlias(String message) {
		return message.trim().toLowerCase().startsWith("#aliasdel");
	}

	private boolean isAddAlias(String message) {
		return message.trim().toLowerCase().startsWith("#alias ");
	}
	
	private boolean isListAlias(String message) {
		return message.trim().equalsIgnoreCase("#aliaslist");
	}

	private ShellCommand processListAliasRequest(ChatAdapterInterface chat, String message) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : aliases.entrySet()) {
			sb.append(entry.getKey()+" : " + entry.getValue()+"\n");
		}
		String reply = "Registered aliases:\n" + sb.toString().trim();
		return new ReplyTextRequest(chat, message, reply);
	}

	private ShellCommand processAliasRequestAndGenerateReply(ChatAdapterInterface chat, String message) {
		String msg = message.replaceAll("#alias\\s+", "");
		String alias = msg.replaceAll("([^ ]*)\\s.*", "$1");
		String expanded = msg.replace(alias, "").trim();
		String actualAlias = "#"+alias;
		aliases.put(actualAlias, expanded);
		
		String reply = String.format("Alias '%s' created to expand to <%s>",actualAlias,expanded);
		ReplyTextRequest request = new ReplyTextRequest(chat, message, reply);
		return request;
	}

	private ShellCommand processRemoveAliasRequest(ChatAdapterInterface chat, String message) {
		String aliasToRemove = message.replaceAll("#aliasdel[ ]*", "");
		String aliasKey = "#"+aliasToRemove;
		final String reply;
		if (aliases.containsKey(aliasKey)){
			aliases.remove(aliasKey);
			reply = String.format("Alias '%s' removed.",aliasToRemove);
		}
		else {
			reply = String.format("Alias '%s' doesn't exist.",aliasToRemove);
		}
		
		ReplyTextRequest request = new ReplyTextRequest(chat, message, reply);
		return request;
	}
	
	private String expandMessageIfNeeded(String message) {
		String candidateAlias = message.replaceAll("(#[^ ]*)\\s.*", "$1");
		String expanded = aliases.get(candidateAlias);
		if (expanded == null)
			return message;
		String arguments = message.replace(candidateAlias, "");
		return expanded+arguments;
	}

}
