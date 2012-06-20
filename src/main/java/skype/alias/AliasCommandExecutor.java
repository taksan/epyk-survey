package skype.alias;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import skype.ChatAdapterInterface;
import skype.shell.Persistence;
import skype.shell.PersistenceImpl;
import skype.shell.ReplyListener;
import skype.voting.CommandExecutor;
import skype.voting.HelpVisitor;

public class AliasCommandExecutor implements CommandExecutor, HelpProvider {
	private final AliasExpander expander;
	private ReplyListener listener;
	
	public AliasCommandExecutor(AliasExpander expander) {
		this.expander = expander;
	}
	
	public AliasCommandExecutor() {
		this(new PersistenceImpl());
	}

	public AliasCommandExecutor(Persistence persistence) {
		this(new AliasExpanderImpl(persistence));
	}
	

	@Override
	public boolean processMessage(ChatAdapterInterface chat, String message) {
		String reply = interpretAndProcessMessage(chat, message);
		if (reply == null)
			return false;
		listener.onReply(chat, reply);
		return true;
	}

	@Override
	public void setReplyListener(ReplyListener listener) {
		this.listener = listener;
	}	

	private String interpretAndProcessMessage(ChatAdapterInterface chat, String message) {
		if (isAddAlias(message))
			return processAliasRequestAndGenerateReply(message);
		if (isListAlias(message))
			return processListAliasRequest();
		if (isRemoveAlias(message))
			return processRemoveAliasRequest(message);
		
		return null;
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

	private String processListAliasRequest() {
		StringBuilder sb = new StringBuilder();
		Map<String, String> aliases = expander.getAliases();
		for (Entry<String, String> entry : aliases.entrySet()) {
			sb.append(entry.getKey()+" : " + entry.getValue()+"\n");
		}
		return "Registered aliases:\n" + sb.toString().trim();
	}

	private String processAliasRequestAndGenerateReply(String message) {
		String msg = message.replaceAll("#alias\\s+", "");
		String alias = msg.replaceAll("([^ ]*)\\s.*", "$1");
		String expanded = msg.replace(alias, "").trim();
		String actualAlias = "#"+alias;		
		expander.createNewAlias(actualAlias, expanded);
		
		return String.format("Alias '%s' created to expand to <%s>",actualAlias,expanded);
	}

	private String processRemoveAliasRequest(String message) {
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
		return reply.get();
	}

	@Override
	public void acceptHelpVisitor(HelpVisitor helpVisitor) {
		helpVisitor.
			onTopLevel("Alias manipulation:").
				onTopic("#alias <aliasname> <expansion>").
					onTopicDescription("creates a new <alias> that will expand to the given text starting when #aliasname is typed").
				onTopic("#aliaslist").
					onTopicDescription("display the existing aliases").
				onTopic("#aliasdel <aliasname>").
					onTopicDescription("removes specified alias");
		
		Map<String, String> aliases = expander.getAliases();
		if (aliases.size() ==0) {
			helpVisitor.onTopLevel("No aliases have been defined yet");
			return;
		}
		helpVisitor.onTopLevel("Currently defined aliases");
		for (Entry<String, String> a : aliases.entrySet()) {
			helpVisitor.onTopic("alias " + a.getKey()+" : " + a.getValue());
		}
	}

}
