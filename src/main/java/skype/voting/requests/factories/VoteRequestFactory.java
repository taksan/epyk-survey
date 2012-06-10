package skype.voting.requests.factories;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandFactory;
import skype.voting.requests.VoteRequest;

public class VoteRequestFactory implements ShellCommandFactory {
	@Override
	public VoteRequest produce(ChatAdapterInterface chat, String message) {
		String optionNUmber = message.trim().replace("#", "");
		int voteNumber = Integer.parseInt(optionNUmber);
		return new VoteRequest(chat, message, voteNumber);
	}

	@Override
	public boolean understands(String message) {
		return message.matches("#[0-9]+");
	}

	@Override
	public String getHelp() {
		return "#<1,2,3...>\n" +
				"	issue your vote for the given option.";
	} 
}