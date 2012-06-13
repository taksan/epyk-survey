package skype.voting;

import skype.ChatAdapterInterface;
import skype.SkypeBridge;
import skype.shell.CommandInterpreter;
import skype.shell.ReplyListener;
import skype.shell.ShellCommand;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

public class VotingPollBroker implements ChatMessageListener, ReplyListener {

	private final CommandInterpreter interpreter;
	private final SkypeBridge skypeBridge;
	private final ShellCommandExecutorInterface processor;

	public VotingPollBroker(
			SkypeBridge skypeBridge, 
			CommandInterpreter interpreter, 
			ShellCommandExecutorInterface processor) {
		this.skypeBridge = skypeBridge;
		this.interpreter = interpreter;
		this.processor = processor;
		processor.setReplyListener(this);
	}

	@Override
	public void chatMessageReceived(ChatMessage receivedChatMessage)
			throws SkypeException {
		ChatAdapterInterface chatAdapter = skypeBridge.getChatAdapter(receivedChatMessage);
		String message = skypeBridge.getContent(receivedChatMessage);
		
		ShellCommand shellCommand = interpreter.processMessage(
				chatAdapter,
				message);
		
		processor.processIfPossible(shellCommand);
	}

	@Override
	public void chatMessageSent(ChatMessage sentChatMessage)
			throws SkypeException {
		ShellCommand shellCommand = interpreter.processMessage(
				skypeBridge.getChatAdapter(sentChatMessage), 
				skypeBridge.getContent(sentChatMessage));
		
		processor.processIfPossible(shellCommand);
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