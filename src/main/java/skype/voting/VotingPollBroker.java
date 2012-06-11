package skype.voting;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.CommandInterpreter;
import skype.shell.CommandProcessor;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

public class VotingPollBroker implements ChatMessageListener, ReplyListener {

	private final CommandInterpreter interpreter;
	private final SkypeBridge skypeBridge;
	private final CommandProcessor processor;

	public VotingPollBroker(
			SkypeBridge skypeBridge, 
			CommandInterpreter interpreter, 
			CommandProcessor processor) {
		this.interpreter = interpreter;
		this.skypeBridge = skypeBridge;
		this.processor = processor;
		processor.addReplyListener(this);
	}

	@Override
	public void chatMessageReceived(ChatMessage receivedChatMessage)
			throws SkypeException {
		System.out.println(skypeBridge.getContent(receivedChatMessage));
		
		ChatAdapterInterface chatAdapter = skypeBridge.getChatAdapter(receivedChatMessage);
		ShellCommand shellCommand = interpreter.processMessage(
				chatAdapter,
				skypeBridge.getContent(receivedChatMessage));
		
		shellCommand.beProcessedAsReceivedMessage(processor);
	}

	@Override
	public void chatMessageSent(ChatMessage sentChatMessage)
			throws SkypeException {
		ShellCommand shellCommand = interpreter.processMessage(
				skypeBridge.getChatAdapter(sentChatMessage), 
				skypeBridge.getContent(sentChatMessage));
		
		shellCommand.beProcessedAsSentMessage(processor);
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