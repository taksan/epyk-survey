package skype.voting.processors.interpreters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;
import skype.voting.application.VotingPollOption;
import skype.voting.processors.interpreters.StartPollInterpreter;
import skype.voting.processors.requests.StartPollRequest;
import skype.voting.processors.requests.VotingPollVisitor;

public class StartPollInterpreterTest {
	@Test
	public void onInvalidMessage_ShouldNotBuildRequest() {
		StartPollInterpreter subject = new StartPollInterpreter();
		assertNull(subject.processMessage(null,"#2"));
	}
	
	@Test
	public void onCorrectCommand_ShouldCreateLunchRequest() {
		StartPollInterpreter subject = new StartPollInterpreter();
		ChatBridgeMock chat = new ChatBridgeMock("#42");
		chat.addParticipant("joe");
		chat.addParticipant("moe");
		StartPollRequest request = subject.processMessage(chat, " #startpoll \"welcome here\" verdinho,garbo");
		
		final StringBuffer sb= new StringBuffer();
		
		request.accept(
				new VotingPollVisitor() {
					public void onWelcomeMessage(String message) {
						sb.append("Welcome: " + message + "\n");
					}
					public void visitOption(VotingPollOption option) {
						sb.append("Option: "+option.getName()+"\n");
					}
					public void visitParticipant(String participantName) {
						sb.append("Participant: " + participantName+"\n");
					}
				});
		
		String expected = 
				"Welcome: welcome here\n"+
				"Option: verdinho\n" +
				"Option: garbo\n" +
				"Participant: joe\n" +
				"Participant: moe";
		assertEquals(expected, sb.toString().trim());
	}
}