package skype.voting.processors.interpreters;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandInterpreter;
import skype.voting.processors.requests.VoteStatusRequest;

public class VoteStatusInterpreter implements ShellCommandInterpreter {

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
