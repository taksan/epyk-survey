package skype.voting.processors.interpreters;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandInterpreter;
import skype.voting.processors.requests.AddVoteOptionRequest;

public class AddVoteOptionRequestInterpreter implements ShellCommandInterpreter {
	@Override
	public AddVoteOptionRequest processMessage(ChatAdapterInterface chat, String message) {
		if (!understands(message))
			return null;
		String optionName = message.replaceAll("#addoption[ ]+", "");
		AddVoteOptionRequest request = new AddVoteOptionRequest(chat, message, optionName);
		return request;
	}

	@Override
	public boolean understands(String message) {
		return message.matches("#addoption[ ]+.*");
	}

	@Override
	public String getHelp() {
		return "#addoption <option name>\n" +
				"	Adds a new poll option with given name. The name can have spaces.";
	}
}
