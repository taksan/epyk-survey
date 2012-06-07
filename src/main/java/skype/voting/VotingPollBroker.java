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

	public VotingPollBroker(SkypeBridge skypeBridge, CommandInterpreter interpreter, CommandProcessor processor) {
		this.interpreter = interpreter;
		this.skypeBridge = skypeBridge;
		this.processor = processor;
		processor.addReplyListener(this);
	}

	public void chatMessageReceived(ChatMessage receivedChatMessage)
			throws SkypeException {
		ShellCommand shellCommand = interpreter.processMessage(
				skypeBridge.getChatAdapter(receivedChatMessage),
				receivedChatMessage.getContent());
		
		shellCommand.acceptProcessorForReceivedMessages(processor);
	}

	@Override
	public void chatMessageSent(ChatMessage sentChatMessage)
			throws SkypeException {
		ShellCommand shellCommand = interpreter.processMessage(
				skypeBridge.getChatAdapter(sentChatMessage), 
				sentChatMessage.getContent());
		
		shellCommand.acceptProcessorForSentMessages(processor);
	}

	@Override
	public void onReply(ChatAdapterInterface chat, String reply) {
		skypeBridge.sendMessage(chat, reply);
	}
}