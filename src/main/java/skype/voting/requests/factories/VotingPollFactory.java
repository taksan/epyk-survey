package skype.voting.requests.factories;

import java.util.List;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandFactory;
import skype.voting.VotingPollOption;
import skype.voting.requests.VotingPollRequest;

public class VotingPollFactory implements ShellCommandFactory {

	@Override
	public VotingPollRequest produce(ChatAdapterInterface chat, String message) {
		return buildRequest(chat, message);
	}

	private VotingPollRequest buildRequest(ChatAdapterInterface chat, String incomingCommand) {
		String command = incomingCommand.trim();
		VotingPollRequest lunchRequest = new VotingPollRequest(chat, command);
		command = command.replaceAll("#startpoll[ ]*", "");
		String welcome = command.replaceAll("\"(.*)\".*","$1");
		String options = command.replaceAll("\".*\"(.*)","$1").trim();
		String[] optionNames = options.split(",");
		for (String aPlace : optionNames) {
			lunchRequest.add(new VotingPollOption(aPlace));
		}
		List<String> partipantNames = chat.getPartipantNames();
		for (String participantName : partipantNames) {
			lunchRequest.addParticipant(participantName);
		}
		lunchRequest.setWelcomeMessage(welcome);
		return lunchRequest;
	}

	public boolean understands(String message) {
		return message.startsWith("#startpoll");
	}

}