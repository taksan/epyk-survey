package skype.voting.requests.factories;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandFactory;
import skype.voting.requests.ClosePollRequest;

public class ClosePollRequestFactory implements ShellCommandFactory {

	@Override
	public ClosePollRequest produce(ChatAdapterInterface chat, String message) {
		if (!understands(message))
			return null;
		return new ClosePollRequest(chat, message);
	}

	public boolean understands(String message) {
		return message.trim().equalsIgnoreCase("#closepoll");
	} 
}