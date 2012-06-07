package skype.voting;

import java.util.List;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandFactory;

public class VotingPollFactory implements ShellCommandFactory {

	@Override
	public VotingPollRequest produce(ChatAdapterInterface chat, String message) {
		if (understands(message)) {
			return buildRequest(chat, message);
		}
		return null;
	}

	private VotingPollRequest buildRequest(ChatAdapterInterface chat, String command) {
		VotingPollRequest lunchRequest = new VotingPollRequest(chat, command);
		command = command.replaceAll("#lunch[ ]*", "");
		String[] optionNames = command.split(",");
		for (String aPlace : optionNames) {
			lunchRequest.add(new VotingPollOption(aPlace));
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