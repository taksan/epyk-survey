package skype.shell;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import skype.ChatAdapterInterface;

public class AliasProcessorImpl implements AliasProcessor {
	private final AliasExpander expander;
	
	public AliasProcessorImpl(AliasExpander expander) {
		this.expander = expander;
	}

	public AliasProcessorImpl(Persistence persistence) {
		this(new AliasExpanderImpl(persistence));
	}

	@Override
	public boolean understands(String message) {
		return isAddAlias(message) || isListAlias(message) || isRemoveAlias(message);
	}
	
	@Override
	public String expandMessage(String message) {
		return expander.expand(message);
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
		Map<String, String> aliases = expander.getAliases();
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
		expander.createNewAlias(actualAlias, expanded);
		
		String reply = String.format("Alias '%s' created to expand to <%s>",actualAlias,expanded);
		ReplyTextRequest request = new ReplyTextRequest(chat, message, reply);
		return request;
	}

	private ShellCommand processRemoveAliasRequest(ChatAdapterInterface chat, String message) {
		final String aliasToRemove = message.replaceAll("#aliasdel[ ]*", "");
		String aliasKey = "#"+aliasToRemove;
		final AtomicReference<String> reply = new AtomicReference<String>();
		expander.removeAlias(aliasKey, new RemoveAliasFeedbackHandler() {
			
			@Override
			public void onSuccess() {
				reply.set(String.format("Alias '%s' removed.",aliasToRemove));
			}
			
			@Override
			public void onAliasDoesNotExist() {
				reply.set(String.format("Alias '%s' doesn't exist.",aliasToRemove));
			}
		});
		
		ReplyTextRequest request = new ReplyTextRequest(chat, message, reply.get());
		return request;
	}

}
