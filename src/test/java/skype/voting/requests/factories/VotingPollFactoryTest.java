package skype.voting.requests.factories;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import skype.shell.mocks.ChatBridgeMock;
import skype.voting.VotingPollOption;
import skype.voting.VotingPollVisitor;
import skype.voting.requests.VotingPollRequest;

public class VotingPollFactoryTest {
	@Test
	public void onCorrectCommand_ShouldCreateLunchRequest() {
		VotingPollFactory subject = new VotingPollFactory();
		ChatBridgeMock chat = new ChatBridgeMock("#42");
		chat.addParticipant("joe");
		chat.addParticipant("moe");
		VotingPollRequest request = subject.produce(chat, " #startpoll \"welcome here\" verdinho,garbo");
		
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
