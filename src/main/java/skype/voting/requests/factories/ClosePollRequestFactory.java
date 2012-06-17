package skype.voting.requests.factories;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandInterpreter;
import skype.voting.requests.ClosePollRequest;

public class ClosePollRequestFactory implements ShellCommandInterpreter {

	@Override
	public ClosePollRequest processMessage(ChatAdapterInterface chat, String message) {
		if (!understands(message))
			return null;
		return new ClosePollRequest(chat, message);
	}

	public boolean understands(String message) {
		return message.trim().equalsIgnoreCase("#closepoll");
	}

	@Override
	public String getHelp() {
		return "#closepoll\n" +
				"	closes the poll and print the winner option";
	} 
}