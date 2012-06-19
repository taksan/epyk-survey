package skype.voting.requests.factories;

import java.util.List;

import skype.ChatAdapterInterface;
import skype.shell.ShellCommandInterpreter;
import skype.voting.application.VotingPollOption;
import skype.voting.requests.StartPollRequest;

public class StartPollInterpreter implements ShellCommandInterpreter {

	@Override
	public StartPollRequest processMessage(ChatAdapterInterface chat, String message) {
		return buildRequest(chat, message);
	}

	private StartPollRequest buildRequest(ChatAdapterInterface chat, String incomingCommand) {
		String command = incomingCommand.trim();
		StartPollRequest votingPollRequest = new StartPollRequest(chat, command);
		command = command.replaceAll("#startpoll[ ]*", "");
		String welcome = command.replaceAll("\"(.*)\".*","$1");
		String options = command.replaceAll("\".*\"(.*)","$1").trim();
		String[] optionNames = options.split(",");
		for (String aPlace : optionNames) {
			votingPollRequest.add(new VotingPollOption(aPlace));
		}
		List<String> partipantNames = chat.getPartipantNames();
		for (String participantName : partipantNames) {
			votingPollRequest.addParticipant(participantName);
		}
		votingPollRequest.setWelcomeMessage(welcome);
		return votingPollRequest;
	}

	public boolean understands(String message) {
		return message.startsWith("#startpoll");
	}

	@Override
	public String getHelp() {
		return "#startpoll \"welcome message\" option1,option2,...\n" +
				"	starts a new poll with given options.";
	}

}