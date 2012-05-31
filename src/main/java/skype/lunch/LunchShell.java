package skype.lunch;

import skype.SkypeBridge;
import skype.shell.CommandInterpreter;
import skype.shell.ShellCommand;
import skype.shell.ShellCommandVisitor;

import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.SkypeException;

public class LunchShell implements ChatMessageListener, ShellCommandVisitor {

	private final CommandInterpreter interpreter;
	private final SkypeBridge skypeBridge;

	public LunchShell(CommandInterpreter interpreter, SkypeBridge skypeBridge) {
		this.interpreter = interpreter;
		this.skypeBridge = skypeBridge;
	}

	public void chatMessageReceived(ChatMessage receivedChatMessage)
			throws SkypeException {
		interpreter.processMessage(receivedChatMessage.getChat().getId(), receivedChatMessage.getContent());
	}

	@Override
	public void chatMessageSent(ChatMessage sentChatMessage)
			throws SkypeException {
		ShellCommand shellCommand = interpreter.processMessage(sentChatMessage.getChat().getId(), sentChatMessage.getContent());
		shellCommand.acceptSentRequest(this);
	}

	@Override
	public void processSentRequest(LunchRequest lunchRequest) {
		final StringBuffer msg = new StringBuffer();
		msg.append("Almoço!\n");
		lunchRequest.accept(new LunchRequestVisitor() {
			int count=1;
			@Override
			public void visit(LunchOption option) {
				msg.append(count+") ");
				msg.append(option.getName());
				msg.append("\n");
				count++;
			}
		});
		skypeBridge.sendMessage(lunchRequest.getChatId(), msg.toString().trim());
	}
}