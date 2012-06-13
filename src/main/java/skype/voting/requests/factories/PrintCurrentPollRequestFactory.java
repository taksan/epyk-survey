package skype.voting.requests.factories;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandFactory;
import skype.voting.requests.PrintCurrentPollRequest;

public class PrintCurrentPollRequestFactory implements ShellCommandFactory {
	
	@Override
	public PrintCurrentPollRequest produce(ChatAdapterInterface chat, String message) {
		if (!understands(message))
			return null;
		return new PrintCurrentPollRequest(chat,message);
	}

	@Override
	public boolean understands(String message) {
		return message.trim().equalsIgnoreCase("#showpoll");
	}
	
	@Override
	public String getHelp() {
		return "#showpoll\n" +
				"	prints current poll options";
	}
}
