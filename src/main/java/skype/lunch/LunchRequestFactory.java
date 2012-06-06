package skype.lunch;

import java.util.List;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandFactory;

public class LunchRequestFactory implements ShellCommandFactory {

	@Override
	public LunchRequest produce(ChatAdapterInterface chat, String message) {
		if (understands(message)) {
			return buildRequest(chat, message);
		}
		return null;
	}

	private LunchRequest buildRequest(ChatAdapterInterface chat, String command) {
		LunchRequest lunchRequest = new LunchRequest(chat, command);
		command = command.replaceAll("#lunch[ ]*", "");
		String[] optionNames = command.split(",");
		for (String aPlace : optionNames) {
			lunchRequest.add(new LunchOption(aPlace));
		}
		List<String> partipantNames = chat.getPartipantNames();
		for (String participantName : partipantNames) {
			lunchRequest.addParticipant(participantName);
		}
		return lunchRequest;
	}

	private boolean understands(String message) {
		return message.startsWith("#lunch");
	}

}