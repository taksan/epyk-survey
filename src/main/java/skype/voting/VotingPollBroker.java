package skype.voting;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.ReplyListener;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

public class VotingPollBroker implements ChatMessageListener, ReplyListener {

	private final SkypeBridge skypeBridge;
	private final CommandExecutor[] commandExecutors;

	public VotingPollBroker(SkypeBridge bridge, CommandExecutor[] commandExecutors) {
		this.skypeBridge = bridge;
		this.commandExecutors = commandExecutors;
		for (CommandExecutor ce : commandExecutors) {
			ce.setReplyListener(this);
		}
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
	
	private void processChatMessage(ChatMessage receivedChatMessage) throws SkypeException {
		ChatAdapterInterface chatAdapter = skypeBridge.getChatAdapter(receivedChatMessage);
		String message = skypeBridge.getContent(receivedChatMessage);
		for (CommandExecutor ce : commandExecutors) {
			ce.processMessage(chatAdapter, message);
		}
	}

	@Override
	public void onReply(ChatAdapterInterface chat, String reply) {
		skypeBridge.sendMessage(chat, reply);
	}

	@Override
	public void onReplyPrivate(ChatAdapterInterface chat, String reply) {
		skypeBridge.sendMessageToUser(chat.getLasterSenderFullName(), reply);
	}
}