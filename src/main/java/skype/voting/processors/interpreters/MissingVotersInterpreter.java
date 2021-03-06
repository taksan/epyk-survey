package skype.voting.processors.interpreters;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandInterpreter;
import skype.voting.processors.requests.MissingVotersRequest;

public class MissingVotersInterpreter implements ShellCommandInterpreter {
	@Override
	public MissingVotersRequest processMessage(ChatAdapterInterface chat, String message) {
		if (!understands(message)) return null;
		return new MissingVotersRequest(chat,message);
	}

	@Override
	public boolean understands(String message) {
		return message.trim().equalsIgnoreCase("#missing");
	} 
	
	@Override
	public String getHelp() {
		return "#missing\n" +
				"	displays who hasn't voted yet";
	}
}