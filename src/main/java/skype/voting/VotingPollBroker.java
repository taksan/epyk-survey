package skype.voting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.alias.HelpProvider;
import skype.shell.HelpCommandExecutor;
import skype.shell.ReplyListener;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

public class VotingPollBroker implements ChatMessageListener, ReplyListener {

	private final SkypeBridge skypeBridge;
	final ArrayList<CommandExecutor> commandExecutors = new ArrayList<CommandExecutor>();

	public VotingPollBroker(SkypeBridge bridge, 
			UnrecognizedRequestExecutor last, 
			CommandExecutor... commandExecutors) {
		this.skypeBridge = bridge;
		List<CommandExecutor> asList = Arrays.asList(commandExecutors);
		
		this.commandExecutors.addAll(asList);
		this.commandExecutors.add(getHelperExecutor(commandExecutors));
		this.commandExecutors.add(last);
		
		for (CommandExecutor commandExecutor : this.commandExecutors) {
			commandExecutor.setReplyListener(this);
		}
	}

	private CommandExecutor getHelperExecutor(CommandExecutor[] commandExecutors2) {
		List<HelpProvider> providers = new ArrayList<HelpProvider>();
		for (CommandExecutor commandExecutor : commandExecutors2) {
			if (commandExecutor instanceof HelpProvider)
				providers.add((HelpProvider) commandExecutor);
		}
		return new HelpCommandExecutor(providers.toArray(new HelpProvider[0]));
	}

	public VotingPollBroker(SkypeBridge bridge, CommandExecutor... commandExecutors){
		this(bridge, new UnrecognizedRequestExecutor(), commandExecutors);
	}

	@Override
	public void chatMessageReceived(ChatMessage receivedChatMessage)
			throws SkypeException {
		ChatAdapterInterface chatAdapter = skypeBridge.getChatAdapter(receivedChatMessage);
		String messageId = skypeBridge.getMessageId(receivedChatMessage);
		String message = skypeBridge.getContent(receivedChatMessage);

		processChatMessage(chatAdapter, messageId, message);
	}


	@Override
	public void chatMessageSent(ChatMessage sentChatMessage)
			throws SkypeException {
		ChatAdapterInterface chatAdapter = skypeBridge.getChatAdapter(sentChatMessage);
		String message = skypeBridge.getContent(sentChatMessage);
		String messageId = skypeBridge.getMessageId(sentChatMessage);
		
		processChatMessage(chatAdapter, messageId, message);
	}
	
	Set<String> idOfBotReplies = new LinkedHashSet<String>();
	@Override
	public void onReply(ChatAdapterInterface chat, String reply) {
		String sentMessageId = skypeBridge.sendMessage(chat, reply);
		idOfBotReplies.add(sentMessageId);
	}

	@Override
	public void onReplyPrivate(ChatAdapterInterface chat, String reply) {
		String sentMessageId = skypeBridge.sendMessageToUser(chat.getLasterSenderFullName(), reply);
		idOfBotReplies.add(sentMessageId);
	}

	synchronized void processChatMessage(ChatAdapterInterface chatAdapter, String messageId, String message) {
		if (idOfBotReplies.contains(messageId)) {
			idOfBotReplies.remove(messageId);
			return;
		}
				
		for (CommandExecutor commandExecutor : commandExecutors) {
			boolean processed = commandExecutor.processMessage(chatAdapter, message);
			if (processed) break;
		}
	}
	
}