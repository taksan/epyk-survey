package skype.voting.requests.factories;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandInterpreter;
import skype.voting.requests.VoteStatusRequest;

public class VoteStatusRequestFactory implements ShellCommandInterpreter {

	@Override
	public boolean understands(String message) {
		return message.trim().toLowerCase().equals("#status");
	}
	
	@Override
	public VoteStatusRequest processMessage(ChatAdapterInterface chat, String message) {
		if (!understands(message))
			return null;
		return new VoteStatusRequest(chat, message);
	}

	@Override
	public String getHelp() {
		return "#status\n" +
				"	prints the current tally for each option";
	}
}
