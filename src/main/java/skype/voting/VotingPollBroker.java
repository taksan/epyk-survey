package skype.voting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		processChatMessage(receivedChatMessage);
	}


	@Override
	public void chatMessageSent(ChatMessage sentChatMessage)
			throws SkypeException {
		processChatMessage(sentChatMessage);
	}
	
	@Override
	public void onReply(ChatAdapterInterface chat, String reply) {
		skypeBridge.sendMessage(chat, reply);
	}

	@Override
	public void onReplyPrivate(ChatAdapterInterface chat, String reply) {
		skypeBridge.sendMessageToUser(chat.getLasterSenderFullName(), reply);
	}

	private synchronized void processChatMessage(ChatMessage receivedChatMessage) throws SkypeException {
		
		ChatAdapterInterface chatAdapter = skypeBridge.getChatAdapter(receivedChatMessage);
		String message = skypeBridge.getContent(receivedChatMessage);
		
		for (CommandExecutor commandExecutor : commandExecutors) {
			boolean processed = commandExecutor.processMessage(chatAdapter, message);
			if (processed) break;
		}
	}
	
}