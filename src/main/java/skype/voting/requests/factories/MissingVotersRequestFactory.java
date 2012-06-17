package skype.voting.requests.factories;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandInterpreter;
import skype.voting.requests.MissingVotersRequest;

public class MissingVotersRequestFactory implements ShellCommandInterpreter {

	@Override
	public String getHelp() {
		return "#missing\n" +
				"	displays who hasn't voted yet";
	}

	@Override
	public MissingVotersRequest processMessage(ChatAdapterInterface chat, String message) {
		if (!understands(message)) return null;
		return new MissingVotersRequest(chat,message);
	}

	@Override
	public boolean understands(String message) {
		return message.trim().equalsIgnoreCase("#missing");
	} 
	
}